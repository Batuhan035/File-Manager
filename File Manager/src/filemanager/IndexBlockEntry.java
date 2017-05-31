/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

/**
 *
 * @author batuhan
 */
public class IndexBlockEntry {
    private int FileIndex;
    private int recordPartNumber;
    private int startIndexInMemory;
    private int recordSize;

    public IndexBlockEntry(int FileInd, int recordPartNumber, int startIndexInMemory, int recordSize) {
        this.FileIndex = FileInd;
        this.recordPartNumber = recordPartNumber;
        this.startIndexInMemory = startIndexInMemory;
        this.recordSize = recordSize;
    }
    
    
    public String [] toStringArray() {
      String [] str = new String[4];
      str[0] = "File "+this.FileIndex;
      str[1] = "Record "+this.recordPartNumber;
      str[2] = ""+this.startIndexInMemory;
      str[3] = this.recordSize+" bytes";
      return str;
    }
     
}
