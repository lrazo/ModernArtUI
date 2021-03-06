package org.lrazo.modernart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {
	
	private WebView mWebView;

	/** Called when the activity is first created. */
	@SuppressLint("SetJavaScriptEnabled") @Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_web);
	    
	    mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new HelloWebViewClient());
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.loadUrl("http://www.moma.org/");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class HelloWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
