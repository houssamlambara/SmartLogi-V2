//package com.houssam.SmartLogi.service;
//
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//
//    public EmailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    // Envoyer un email simple
//    public void envoyerEmail(String destinataire, String sujet, String contenu) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(destinataire);
//        message.setSubject(sujet);
//        message.setText(contenu);
//        message.setFrom("votre-email@gmail.com");
//
//        mailSender.send(message);
//    }
//
//    // Envoyer à plusieurs destinataires
//    public void envoyerEmailMultiple(String[] destinataires, String sujet, String contenu) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(destinataires);
//        message.setSubject(sujet);
//        message.setText(contenu);
//        message.setFrom("votre-email@gmail.com");
//
//        mailSender.send(message);
//    }
//}
