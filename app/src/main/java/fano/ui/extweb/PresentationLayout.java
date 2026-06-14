package fano.ui.extweb;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class PresentationLayout extends Presentation {
    public PresentationLayout(Context c, Display d){
        super(c, d, R.style.Theme_ExtWeb);
    }
    WebView webView;
    WebSettings settings;
    LinearProgressIndicator h;
    ImageView cursor;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private FrameLayout fullscreenContainer;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_layout);
        webView=findViewById(R.id.webview);
        h=findViewById(R.id.linearProgressIndicator);
        cursor = findViewById(R.id.cursor);
        fullscreenContainer = findViewById(R.id.fullscreen_container);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                h.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                h.setVisibility(View.GONE);
            }

        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                h.setProgress(newProgress,true);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (customView != null) {
                    onHideCustomView();
                    return;
                }
                customView = view;
                customViewCallback = callback;
                fullscreenContainer.addView(customView);
                fullscreenContainer.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onHideCustomView() {
                if (customView == null) return;
                fullscreenContainer.removeView(customView);
                customView = null;
                fullscreenContainer.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                if (customViewCallback != null) customViewCallback.onCustomViewHidden();
            }
        });
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        webView.loadUrl("https://www.google.com");
    }
    public void refreshWeb(){
        if (webView != null) {
            webView.reload();
        }
    }
    public void goBck(){
        if (customView != null) {
            webView.getWebChromeClient().onHideCustomView();
        } else if (webView != null && webView.canGoBack()){
            webView.goBack();
        }
    }

    public void pageUp() {
        if (webView != null) webView.pageUp(false);
    }

    public void pageDown() {
        if (webView != null) webView.pageDown(false);
    }

    public void pageLeft() {
        if (webView != null) {
            webView.scrollBy(-webView.getWidth() / 2, 0);
        }
    }

    public void pageRight() {
        if (webView != null) {
            webView.scrollBy(webView.getWidth() / 2, 0);
        }
    }

    public void zoomIn() {
        if (webView != null) webView.zoomIn();
    }

    public void zoomOut() {
        if (webView != null) webView.zoomOut();
    }

    public void injectKey(int keyCode) {
        if (webView != null) {
            webView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
            webView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyCode));
        }
    }

    public void injectText(String text) {
        if (webView != null && text != null) {
            // Using JavaScript to inject text into the active element
            String escapedText = text.replace("'", "\\'");
            String script = "var el = document.activeElement; " +
                    "if (el && (el.tagName === 'INPUT' || el.tagName === 'TEXTAREA')) { " +
                    "   var start = el.selectionStart; " +
                    "   var end = el.selectionEnd; " +
                    "   el.value = el.value.substring(0, start) + '" + escapedText + "' + el.value.substring(end); " +
                    "   el.selectionStart = el.selectionEnd = start + " + text.length() + "; " +
                    "   el.dispatchEvent(new Event('input', { bubbles: true })); " +
                    "   el.dispatchEvent(new Event('change', { bubbles: true })); " +
                    "}";
            webView.evaluateJavascript(script, null);
        }
    }

    public void moveCursor(float dx, float dy) {
        if (cursor != null) {
            float newX = cursor.getX() + dx;
            float newY = cursor.getY() + dy;

            // Constrain within screen bounds
            newX = Math.max(0, Math.min(newX, getWindow().getDecorView().getWidth() - cursor.getWidth()));
            newY = Math.max(0, Math.min(newY, getWindow().getDecorView().getHeight() - cursor.getHeight()));

            cursor.setX(newX);
            cursor.setY(newY);

            // DISPATCH HOVER: This makes the website "see" the mouse moving
            if (webView != null) {
                float x = newX + cursor.getWidth() / 2f;
                float y = newY + cursor.getHeight() / 2f - webView.getY();

                long time = SystemClock.uptimeMillis();
                MotionEvent hoverEvent = MotionEvent.obtain(time, time, MotionEvent.ACTION_HOVER_MOVE, x, y, 0);
                hoverEvent.setSource(InputDevice.SOURCE_MOUSE); // Identify as hardware mouse
                webView.dispatchGenericMotionEvent(hoverEvent);
            }
        }
    }

    public void clickAtCursor() {
        if (webView != null && cursor != null) {
            float x = cursor.getX() + cursor.getWidth() / 2f;
            float y = cursor.getY() + cursor.getHeight() / 2f;

            // Adjust y if webView doesn't start at top (e.g. if h is visible)
            float webViewY = webView.getY();
            float relativeY = y - webViewY;

            long downTime = SystemClock.uptimeMillis();
            MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, relativeY, 0);
            downEvent.setSource(InputDevice.SOURCE_MOUSE);

            MotionEvent upEvent = MotionEvent.obtain(downTime, SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, relativeY, 0);
            upEvent.setSource(InputDevice.SOURCE_MOUSE);

            webView.dispatchTouchEvent(downEvent);
            webView.dispatchTouchEvent(upEvent);
        }
    }
}
