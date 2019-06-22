package com.soeSunmiPrinter.cordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sunmi.extprinterservice;

/**
 * This class echoes a string called from JavaScript.
 */
public class SoeSunmiPrinterPlugin extends CordovaPlugin {
  private static final String TAG = "SoeSunmiPrinter";
  private ExtPrinterService extInterface;

  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      extInterface = ExtPrinterService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }
  };
  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Intent intent = new Intent(); 
    intent.setPackage("com.sunmi.extprinterservice"); 
    intent.setAction("com.sunmi.extprinterservice.PrinterService"); 
    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE); 
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

  public void printColumnsText(JSONArray colsTextArr, JSONArray colsWidthArr, JSONArray colsAlign,
      final CallbackContext callbackContext) {
    final String[] clst = new String[colsTextArr.length()];
    for (int i = 0; i < colsTextArr.length(); i++) {
      try {
        clst[i] = colsTextArr.getString(i);
      } catch (JSONException e) {
        clst[i] = "-";
        Log.i(TAG, "ERROR TEXT: " + e.getMessage());
      }
    }
    final int[] clsw = new int[colsWidthArr.length()];
    for (int i = 0; i < colsWidthArr.length(); i++) {
      try {
        clsw[i] = colsWidthArr.getInt(i);
      } catch (JSONException e) {
        clsw[i] = 1;
        Log.i(TAG, "ERROR WIDTH: " + e.getMessage());
      }
    }
    final int[] clsa = new int[colsAlign.length()];
    for (int i = 0; i < colsAlign.length(); i++) {
      try {
        clsa[i] = colsAlign.getInt(i);
      } catch (JSONException e) {
        clsa[i] = 0;
        Log.i(TAG, "ERROR ALIGN: " + e.getMessage());
      }
    }
    extInterface.printColumnsText(clst, clsw, clsa);
    unbindService(serviceConnection);
  }

}
