package com.naive.gn.plugin.dict.action;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.components.JBList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Vector;

/**
 * Naive-GN
 * Created by guoning on 15/10/29.
 */
public class DictAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor;
        if (project != null) {
            editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            String _text;
            if (editor != null) {
                _text = editor.getSelectionModel().getSelectedText();
                Gson gson = new Gson();
                DictBean dictBean = gson.fromJson(doGet(_text), DictBean.class);

                Vector<String> listData = new Vector<>();
                listData.addAll(dictBean.translation);
                if (dictBean.base != null) {
                    listData.addAll(dictBean.base.explains);
                }
                for (Web web : dictBean.web) {
                    String dict = "";
                    for (String _dict : web.value) {
                        dict += "," + _dict;
                    }

                    listData.add(web.key + "\t" + dict);
                }

                JBList jbList = new JBList(listData);
                PopupChooserBuilder popupChooserBuilder = new PopupChooserBuilder(jbList);
                popupChooserBuilder.createPopup().showInBestPositionFor(editor);
//                popupChooserBuilder.addListener(new JBPopupListener() {
//                    @Override
//                    public void beforeShown(LightweightWindowEvent event) {
//
//                    }
//
//                    @Override
//                    public void onClosed(LightweightWindowEvent event) {
//                        CopyPasteManager copyPasteManager = CopyPasteManager.getInstance();
//                        copyPasteManager.setContents(new StringSelection(event.toString()));
//                    }
//                });

            }
        }
    }

    public static String doGet(String world) {
        String _url = "http://fanyi.youdao.com/openapi.do?keyfrom=SeekBetterMe&key=164530784&type=data&doctype=json&version=1.1&q=";
        StringBuilder resultBuffer = new StringBuilder();
        try {
            URL localURL = new URL(_url + URLEncoder.encode(world, "UTF-8"));
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader reader = null;
            String tempLine = null;

            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            try {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);

                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                }

            } finally {

                if (reader != null) {
                    reader.close();
                }

                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultBuffer.toString();
    }

}
