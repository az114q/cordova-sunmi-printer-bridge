package com.soeSunmiPrinter.cordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.IntentFilter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Outline;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import com.sunmi.extprinterservice.ExtPrinterService;

/**
 * This class echoes a string called from JavaScript.
 */
public class SoeSunmiPrinterPlugin extends CordovaPlugin {
  private static final String TAG = "SoeSunmiPrinter";
  private ExtPrinterService extPrinterService = null;
  private ServiceConnection connService = new ServiceConnection() {
    @Override
    public void onServiceDisconnected(ComponentName name) {
      extPrinterService = null;
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      extPrinterService = ExtPrinterService.Stub.asInterface(service);
    }
  };

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Intent intent = new Intent();
    intent.setPackage("com.sunmi.extprinterservice");
    intent.setAction("com.sunmi.extprinterservice.PrinterService");
    Context.bindService(intent, connService, Context.BIND_AUTO_CREATE);
  }


  @Override
  public void onDestroy() {
    if (extPrinterService != null) {
      Context.unbindService(connService);
    }
    super.onDestroy();
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("coolMethod")) {
      String message = args.getString(0);
      this.coolMethod(message, callbackContext);
      return true;
    } else if (action.equals("printColumnsText")) {
      this.printColumnsText(args.getJSONArray(0), args.getJSONArray(1), args.getJSONArray(2));
      return true;
    } else if (action.equals("printerInit")) {
      this.printerInit();
      return true;
    } else if (action.equals("cutPaper")) {
      this.cutPaper(args.getInt(0), args.getInt(1));
      return true;
    }
    return false;
  }

  private void coolMethod(String message, CallbackContext callbackContext) {
    if (message != null && message.length() > 0) {
      callbackContext.success(message);
    } else {
      callbackContext.error("Expected one non-empty string argument.");
    }
  }

  public void printerInit() {
    try {
      extPrinterService.printerInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void printColumnsText(JSONArray colsTextArr, JSONArray colsWidthArr, JSONArray colsAlign) {
    final String[] clst = new String[colsTextArr.length()];
    for (int i = 0; i < colsTextArr.length(); i++) {
      try {
        clst[i] = colsTextArr.getString(i);
      } catch (JSONException e) {
        clst[i] = "-";
      }
    }
    final int[] clsw = new int[colsWidthArr.length()];
    for (int i = 0; i < colsWidthArr.length(); i++) {
      try {
        clsw[i] = colsWidthArr.getInt(i);
      } catch (JSONException e) {
        clsw[i] = 1;
      }
    }
    final int[] clsa = new int[colsAlign.length()];
    for (int i = 0; i < colsAlign.length(); i++) {
      try {
        clsa[i] = colsAlign.getInt(i);
      } catch (JSONException e) {
        clsa[i] = 0;
      }
    }
    try {
      extPrinterService.printColumnsText(clst, clsw, clsa);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void cutPaper(int mode, int distance) {
    try {
      extPrinterService.cutPaper(mode, distance);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
