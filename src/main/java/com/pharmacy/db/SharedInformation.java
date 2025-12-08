/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pharmacy.db;

import javax.swing.JFrame;

/**
 *
 * @author PC
 */
public class SharedInformation {
    private static String loginUsername = "";
    private static JFrame lastFrame;
    
    public static String getUsername(){
        return loginUsername;
    }
    
    public static void setUsername(String s){
        SharedInformation.loginUsername = s;
    }
    
    public static JFrame getFrame(){
        return lastFrame;
    }
    
    public static void setFrame(JFrame s){
        SharedInformation.lastFrame = s;
    }
}
