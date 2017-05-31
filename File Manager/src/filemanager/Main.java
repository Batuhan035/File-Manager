/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author batuhan
 */
public class Main {


    public static void main(String[] args)  {
          FileManager m = new FileManager();
          m.downloadSampleFiles();
          File desktop = new File(System.getProperty("user.home"), "Desktop");
          File f1 = new File(desktop,"SampleFiles");
          byte [] memory = new byte[10000];
          ArrayList<IndexBlockEntry> blockEntries = new ArrayList<>();
          m.executeCommands(new File("order.txt"),memory,f1.listFiles(),blockEntries);
          System.out.println(blockEntries.size());
          DisplayForm form = new DisplayForm(blockEntries);
          
    }

}
