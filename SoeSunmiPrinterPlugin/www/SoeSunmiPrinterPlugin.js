var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
  exec(success, error, 'SoeSunmiPrinterPlugin', 'coolMethod', [arg0]);
};
exports.printerInit = function () {
  return new Promise(function (resolve, reject) {
    cordova.exec(resolve, reject, "SoeSunmiPrinterPlugin", "printerInit", []);
  });
},
exports.printColumnsText = function (colsTextArr, colsWidthArr, colsAlign) {
    return new Promise(function (resolve, reject) {
      exec(resolve, reject, "SoeSunmiPrinterPlugin", "printColumnsText", [colsTextArr, colsWidthArr, colsAlign]);
    });
  };
exports.cutPaper = function (mode, distance) {
  return new Promise(function (resolve, reject) {
    exec(resolve, reject, "SoeSunmiPrinterPlugin", "cutPaper", [mode, distance]);
  });
};