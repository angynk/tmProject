package com.journaldev.processing.saveData;



import java.io.InputStream;


public interface DataProcesor {

    boolean processDataFromFile(InputStream inputStream);

}
