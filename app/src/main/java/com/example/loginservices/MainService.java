package com.example.loginservices;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
public class MainService extends Service {
    public MainService() {
    }
    //Constant for file for current service
    final static int SAVE_TO_FILE = 1;
    //String which is going to be written in file for current service
    String passedString;
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_TO_FILE:
                    Bundle bundle=(Bundle)msg.obj;
                    passedString=bundle.getString("msg");
                    SaveInputToFile(passedString);
                    Toast.makeText(getApplicationContext(),passedString,Toast.LENGTH_SHORT).show();
                    break;
                    default:  super.handleMessage(msg);
            }
        }
    }
    private void SaveInputToFile(String timeStampedInput) {
        String filename = "LoggingServiceLog.txt";
        File externalFile;
        //Environment:-it tacks the all home envirnmental storage variables
        externalFile = new File(Environment.getExternalStoragePublicDirectory(
                //we can change the directory.
                Environment.DIRECTORY_DOWNLOADS), filename);
        try {
            //we are read,write and exicuting the files in the application.
            FileOutputStream fOutput;
            fOutput = new FileOutputStream(externalFile, true);
            OutputStreamWriter wOutput = new OutputStreamWriter(fOutput);
            wOutput.append(timeStampedInput + "\n");
            wOutput.flush();//clean the file
            wOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Target we publish for client to send messages to IncomingHandler.
    final Messenger nmessanger=new Messenger(new IncomingHandler());
        @Override
        public IBinder onBind(Intent intent) {
            return nmessanger.getBinder();
        }
    }


