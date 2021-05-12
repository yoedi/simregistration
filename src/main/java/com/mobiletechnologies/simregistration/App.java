package com.mobiletechnologies.simregistration;

import com.mobiletechnologies.simregistration.model.Registration;
import com.mobiletechnologies.simregistration.model.value.SimType;
import com.mobiletechnologies.simregistration.util.FileAccess;
import org.jboss.logging.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) {
////        Pattern pattern = Pattern.compile("^\\d{10}$");
////        Matcher matcher = pattern.matcher("2055550125");
//        String pat = "^\\+\\d{1,3}[\\d]$";
//        String text = "+212";
////        String text = "1234567892";
//
//        Pattern pattern = Pattern.compile(pat);
//        Matcher matcher = pattern.matcher(text);
//
//        System.out.println("Hasil = "+matcher.matches());

//        App app = new App();
//        app.print();

        FileAccess fa = new FileAccess();
        List<Registration> registrationList = fa.readFile(args[0]);

        registrationList.forEach(System.out::println);

    }

    public void print() {
        log.info("HALLLLLOOOO");
    }

}
