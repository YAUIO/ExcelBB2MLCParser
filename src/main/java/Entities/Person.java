package Entities;

import Annotations.AlternateTitle;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Objects;

public class Person {
    @AlternateTitle("WRITER LAST NAME *")
    public String LastName;

    @AlternateTitle("WRITER FIRST NAME")
    public String FirstName;

    @AlternateTitle("WRITER ROLE CODE *")
    public String code;

    private enum role_codes {C, A, CA}

    public Person(String name, String role_code) {

        if (!name.isEmpty() || !name.isBlank()) {
            while (name.charAt(0) == ' ') {
                name = name.substring(1);
                if (name.isEmpty() || name.isBlank()) {
                    break;
                }
            }
        }


        if (!name.isEmpty() && !name.isBlank()) {
            String[] nameArr = name.split(" ");

            switch (nameArr.length) {
                case 2:
                    if (Init.getEntityManager().createQuery("SELECT b from Human b WHERE b.name=:name").setParameter("name", nameArr[0]).getSingleResultOrNull() != null) {
                        FirstName = nameArr[0];
                        LastName = nameArr[1];
                    } else if (Init.getEntityManager().createQuery("SELECT b from Human b WHERE b.name=:name").setParameter("name", nameArr[1]).getSingleResultOrNull() != null) {
                        FirstName = nameArr[1];
                        LastName = nameArr[0];
                    } else {
                        JDialog jd = new JDialog();
                        jd.setTitle("Need manual intervention");
                        Dimension d = new Dimension(400, 200);
                        jd.setSize(d);
                        jd.setPreferredSize(d);
                        jd.setResizable(false);
                        jd.setLayout(new GridLayout(2, 1));
                        jd.add(new JLabel("Which is name: (1) " + nameArr[0] + " or (2) " + nameArr[1]));
                        JPanel jp = new JPanel();
                        JButton one = new JButton("(1)");
                        JButton two = new JButton("(2)");
                        jp.add(one);
                        jp.add(two);
                        jd.add(jp);
                        jd.setVisible(true);
                        Thread wait = Thread.currentThread();

                        one.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyPressed(KeyEvent e) {
                                if (e.getKeyCode() == KeyEvent.VK_1) {
                                    Human h = new Human();
                                    h.name = nameArr[0];
                                    EntityManager em = Init.getEntityManager();
                                    em.getTransaction().begin();
                                    em.persist(h);
                                    em.getTransaction().commit();
                                    FirstName = h.name;
                                    LastName = nameArr[1];
                                    jd.setVisible(false);
                                    jd.dispose();
                                    synchronized (wait) {
                                        wait.interrupt();
                                    }
                                } else if (e.getKeyCode() == KeyEvent.VK_2) {
                                    Human h = new Human();
                                    h.name = nameArr[1];
                                    EntityManager em = Init.getEntityManager();
                                    em.getTransaction().begin();
                                    em.persist(h);
                                    em.getTransaction().commit();
                                    FirstName = h.name;
                                    LastName = nameArr[0];
                                    jd.setVisible(false);
                                    jd.dispose();
                                    synchronized (wait) {
                                        wait.interrupt();
                                    }
                                }
                            }
                        });

                        one.addActionListener(l -> {
                            Human h = new Human();
                            h.name = nameArr[0];
                            EntityManager em = Init.getEntityManager();
                            em.getTransaction().begin();
                            em.persist(h);
                            em.getTransaction().commit();
                            FirstName = h.name;
                            LastName = nameArr[1];
                            jd.setVisible(false);
                            jd.dispose();
                            synchronized (wait) {
                                wait.interrupt();
                            }
                        });

                        two.addActionListener(l -> {
                            Human h = new Human();
                            h.name = nameArr[1];
                            EntityManager em = Init.getEntityManager();
                            em.getTransaction().begin();
                            em.persist(h);
                            em.getTransaction().commit();
                            FirstName = h.name;
                            LastName = nameArr[0];
                            jd.setVisible(false);
                            jd.dispose();
                            synchronized (wait) {
                                wait.interrupt();
                            }
                        });

                        try {
                            synchronized (wait) {
                                wait.wait();
                            }
                        } catch (InterruptedException _) {
                        }
                    }
                    break;
                case 1:
                    LastName = nameArr[0];
                    break;
                default:
                    LastName = "UNCONVERTIBLE " + name;
            }
        }

        try {
            code = role_codes.valueOf(role_code).toString();
        } catch (Exception _) {
            throw new RuntimeException("Couldn't parse creator role code: " + role_code);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        if (LastName != null && LastName.equals(person.LastName)) {
            if (FirstName != null) {
                return FirstName.equals(person.FirstName);
            }
        }
        return false;
    }
}
