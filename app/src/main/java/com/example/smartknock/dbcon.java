package com.example.smartknock;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class dbcon {

    Connection con;

    @SuppressLint("NewApi")
    public Connection conclass() {
        String ip = "10.0.2.2", port = "1433", db = "doorbell";
        StrictMode.ThreadPolicy a = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(a);
        String connectURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + db  + ";";


            con = DriverManager.getConnection(connectURL);
            Log.d("DBConnection", "Connecting to: " + connectURL);
            return con;
        } catch (Exception e) {
            Log.e("DBConnection", "Error: " + e.getMessage(), e);

            return null;
        }
    }
}

