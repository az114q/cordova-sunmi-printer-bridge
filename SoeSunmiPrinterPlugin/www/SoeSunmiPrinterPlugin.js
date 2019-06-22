var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
  exec(success, error, 'SoeSunmiPrinterPlugin', 'coolMethod', [arg0]);
};
exports.printColumnsText = function (colsTextArr, colsWidthArr, colsAlign) {
  return new Promise(function (resolve, reject) {
    exec(resolve, reject, "Printer", "printColumnsText", [colsTextArr, colsWidthArr, colsAlign]);
  });
};