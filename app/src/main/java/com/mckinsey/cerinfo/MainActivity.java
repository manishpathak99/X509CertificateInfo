package com.mckinsey.cerinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        showCertificateInfo(this, getPackageName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    static void showCertificateInfo(Activity activity, String packageName) {
        X509Certificate[] certs = Utils.getX509Certificates(activity, packageName);
        if (certs == null || certs.length < 1)
            return;
        /*
         * for now, just support the first cert since that is far and away the
         * most common
         */
        X509Certificate cert = certs[0];

        PublicKey publickey = cert.getPublicKey();
        int size;
        if (publickey.getAlgorithm().equals("RSA"))
            size = ((RSAPublicKey) publickey).getModulus().bitLength();
        else
            size = publickey.getEncoded().length * 7; // bad estimate

        TextView algorithm = (TextView) activity.findViewById(R.id.tv_certificate);
//        algorithm.setText(publickey.getAlgorithm() + " " + String.valueOf(size) + "bit");
//        TextView keySize = (TextView) activity.findViewById(R.id.signature_type);
//        keySize.setText(cert.getSigAlgName());
//        TextView version = (TextView) activity.findViewById(R.id.version);
//        version.setText(String.valueOf(cert.getVersion()));
//
//        TextView issuerdn = (TextView) activity.findViewById(R.id.issuerdn);
//        issuerdn.setText(cert.getIssuerDN().getName());
//        TextView subjectdn = (TextView) activity.findViewById(R.id.subjectdn);
//        subjectdn.setText(cert.getSubjectDN().getName());
//        TextView serial = (TextView) activity.findViewById(R.id.serial);
//        serial.setText(cert.getSerialNumber().toString(16));
//        TextView created = (TextView) activity.findViewById(R.id.created);
//        created.setText(cert.getNotBefore().toLocaleString());
//        TextView expires = (TextView) activity.findViewById(R.id.expires);
//        expires.setText(cert.getNotAfter().toLocaleString());
//
//        TextView md5 = (TextView) activity.findViewById(R.id.MD5);
//        md5.setText(Utils.getCertificateFingerprint(cert, "MD5").toLowerCase(Locale.ENGLISH));
//        TextView sha1 = (TextView) activity.findViewById(R.id.sha1);
//        sha1.setText(Utils.getCertificateFingerprint(cert, "SHA1").toLowerCase(Locale.ENGLISH));
//        TextView sha256 = (TextView) activity.findViewById(R.id.sha256);
//        sha256.setText(Utils.getCertificateFingerprint(cert, "SHA-256").toLowerCase(Locale.ENGLISH));

        StringBuilder sb = new StringBuilder();
        sb.append("ALGORITHM \n" + publickey.getAlgorithm() + " " + String.valueOf(size) + "bit").append("\n\n");
        sb.append("ALGO NAME \n" + cert.getSigAlgName()).append("\n\n");
        sb.append("VERSION \n" + cert.getVersion()).append("\n\n");
        sb.append("ISSUERDN \n" + cert.getIssuerDN().getName()).append("\n\n");
        sb.append("SUBJECTDN \n" + cert.getSubjectDN().getName()).append("\n\n");
        sb.append("ALGORITHM ID \n" + cert.getSigAlgOID()).append("\n\n");
        sb.append("SERIAL \n" + cert.getSerialNumber().toString(16)).append("\n\n");
        sb.append("\n\n\n");

        sb.append("CREATED \n" + cert.getNotBefore().toLocaleString()).append("\n\n");
        sb.append("EXPIRES \n" + cert.getNotAfter().toLocaleString()).append("\n\n");

        sb.append("MD5 \n" + Utils.getCertificateFingerprint(cert, "MD5").toLowerCase(Locale.ENGLISH)).append("\n\n");
        sb.append("SHA1 \n" + Utils.getCertificateFingerprint(cert, "SHA1").toLowerCase(Locale.ENGLISH)).append("\n\n");
        sb.append("SHA-256 \n" + Utils.getCertificateFingerprint(cert, "SHA-256").toLowerCase(Locale.ENGLISH)).append("\n\n");

        TextView details = (TextView) activity.findViewById(R.id.details);
        details.setText(sb.toString());
    }

}
