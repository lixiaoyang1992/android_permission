package cn.lixiaoyang.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private Activity thisActivity;
  private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 396;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    thisActivity = this;
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        if (ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {

          if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
              Manifest.permission.SEND_SMS)) {
            //用户不给权限的友好提示,引导用户去权限设置页
            alertNeedPermission();
          } else {

            // 获取权限

            ActivityCompat.requestPermissions(thisActivity,
                new String[] { Manifest.permission.SEND_SMS }, MY_PERMISSIONS_REQUEST_SEND_SMS);

            // 回调的requestCode
          }
        } else {
          sendSMS();
        }
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onRequestPermissionsResult(int requestCode, String permissions[],
      int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_SEND_SMS: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

          sendSMS();
          // permission was granted, yay! Do the
          // contacts-related task you need to do.

        } else {

          alertNeedPermission();
          // permission denied, boo! Disable the
          // functionality that depends on this permission.
        }
        return;
      }

      // other 'case' lines to check for other
      // permissions this app might request
    }
  }

  public void alertNeedPermission() {
    new AlertDialog.Builder(MainActivity.this).setMessage("app需要开启权限才能使用此功能")
        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
          }
        })
        .setNegativeButton("取消", null)
        .create()
        .show();
  }

  public void sendSMS() {
    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + 10086));
    intent.putExtra("sms_body", "短信");
    startActivity(intent);
  }
}
