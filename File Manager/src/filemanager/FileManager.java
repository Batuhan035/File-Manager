/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author batuhan
 */
public class FileManager {

    public void downloadSampleFiles() {
        try {
            String baseURL = "http://www.textfiles.com/art/LOGOS/";

            Document doc = Jsoup.connect(baseURL).get();
            Elements fileElements = doc.select("td[align=TOP] a[href]");

            for (int i = 0; i < fileElements.size(); i++) {
                try {
                    String x = baseURL + fileElements.get(i).text();
                    URL fileURL = new URL(x);
                    HttpURLConnection httpConn = (HttpURLConnection) fileURL.openConnection();
                    int responseCode = httpConn.getResponseCode();
                    String fileName = "";
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        fileName = x.substring(x.lastIndexOf("/") + 1,
                                x.length());
                    }
                    File desktop = new File(System.getProperty("user.home"), "Desktop");
                    File destFolder = new File(desktop, "SampleFiles");
                    destFolder.mkdirs();
                    String saveFilePath = destFolder.getAbsolutePath() + File.separator + fileName;

                    InputStream inputStream = httpConn.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                    int bytesRead = -1;
                    byte[] buffer = new byte[4096];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.close();
                    inputStream.close();

                    System.out.println("File " + (i + 1) + " downloaded");
                } catch (MalformedURLException ex) {
                    Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] readFileIntoMemory(File file) {
        byte[] fileBytes = null;
        try {
            fileBytes = Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            System.err.println("Error while reading file!");
        }
        return fileBytes;
    }

    public void writeFileIntoDisk(byte[] file, String filename) {
        try {
            FileOutputStream stream = new FileOutputStream(filename);
            stream.write(file);
            stream.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found!");
        } catch (IOException ex) {
            System.err.println("Error while writing file!");
        }

    }

    public void allocateRecordInMemory(byte[] fileBytes, byte[] memory, int memStartIndex) {
        int memCounter = memStartIndex;
        for (int i = 0; i < fileBytes.length; i++) {
            memory[memCounter] = fileBytes[i];
            memCounter++;
        }
    }
    
    public byte[] getRecordFromFile(int startIndex,int endIndex, byte[] fileBytes){
       byte [] record  = Arrays.copyOfRange(fileBytes,startIndex, endIndex);
       return record;
    }
    
    public byte[] divideFileToRecords(byte [] fileBytes , int RequestedPart){
      int dividePoint = fileBytes.length % 2 == 0 ? fileBytes.length/2 :(fileBytes.length/2)+1;
      byte [] firstRecord = new byte[dividePoint];
      byte [] secondRecord = new byte[fileBytes.length - dividePoint];
    
        for (int i = 0; i < dividePoint; i++) {
            firstRecord[i] = fileBytes[i];
        }
        int counter = 0;
        for (int i = dividePoint; i < fileBytes.length; i++) {
            secondRecord[counter] = fileBytes[i];
            counter++;
        }
        if(RequestedPart == 1){
          return firstRecord;
        }
        else{
         return secondRecord;
        }
    }
    
    public byte[] mergeRecordsIntoFile(byte[] rec1,byte[] rec2){
       byte [] file = new byte[rec1.length + rec2.length];
       
       for (int i = 0; i < rec1.length; i++) {
            file[i] = rec1[i];
        }
         int counter = 0;
        for (int i = rec1.length; i < rec1.length+rec2.length; i++) {
            file[i] = rec2[counter];
            counter++;
        }
        return file;
    }
    
    public void executeCommands(File commandFile ,byte[] memory,File[] files, ArrayList<IndexBlockEntry>indexBlock) {
        try {
            Scanner sc = new Scanner(commandFile);
            int allocateCounter = 0;
            while (sc.hasNext()) {                
                String nextCommand = sc.next();
                if(nextCommand.startsWith("A")){
                  int fileIndex = Integer.parseInt(nextCommand.substring(nextCommand.indexOf("*")+1,nextCommand.lastIndexOf("*")));
                  int partNumber = Integer.parseInt(nextCommand.substring(nextCommand.lastIndexOf("*")+1));
                   byte [] fileBytes =  readFileIntoMemory(files[fileIndex]);
                   byte [] recordtoSend=divideFileToRecords(fileBytes, partNumber);
                   allocateRecordInMemory(recordtoSend,memory,allocateCounter);
                   indexBlock.add(new IndexBlockEntry(fileIndex, partNumber, allocateCounter,recordtoSend.length));
                   allocateCounter+=recordtoSend.length;
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Command File not Found!");
        }
      
    }
    

}
