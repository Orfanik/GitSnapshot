/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.Database;
import git.GitRepo;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import model.Repo;
import model.RepoEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

/**
 *
 * @author U292156
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        
        DefaultListModel<String> model = (DefaultListModel<String>)(ctrlMessages.getModel());
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            model.addElement(ex.toString());
        }
        
    }

    public void Init() {
      ctrlRepository.removeAllItems();
      try {
        ResultSet rs = Repo.fetchAll();
        String wrkDir = new String();
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>)(ctrlRepository.getModel());
        while (rs.next()) {
          Repo adat = new Repo(rs);
          model.addElement(adat.getNeve());
          if (wrkDir.isEmpty()) {
             wrkDir = adat.getNeve();
          }
        }
      } catch (SQLException ex) {
        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ctrlMessages = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        ctrlSearchRepository = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        ctrlIssueId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        ctrlZipFilePrefix = new javax.swing.JTextField();
        ctrlSearchOutputDir = new javax.swing.JButton();
        ctrlStart = new javax.swing.JButton();
        ctrlOk = new javax.swing.JButton();
        ctrlRepository = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        ctrlBranch = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        ctrlFullVersion = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("res/Bundle"); // NOI18N
        setTitle(bundle.getString("MainTitle")); // NOI18N

        ctrlMessages.setModel(new DefaultListModel<String>());
        jScrollPane1.setViewportView(ctrlMessages);

        jLabel1.setText(bundle.getString("Repository")); // NOI18N

        ctrlSearchRepository.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/orange_folder_saved_search_22.png"))); // NOI18N
        ctrlSearchRepository.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrlSearchRepositoryActionPerformed(evt);
            }
        });

        jLabel2.setText(bundle.getString("IssueId")); // NOI18N

        jLabel3.setText(bundle.getString("ZipBaseName")); // NOI18N

        ctrlZipFilePrefix.setToolTipText("");

        ctrlSearchOutputDir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/orange_folder_saved_search_22.png"))); // NOI18N
        ctrlSearchOutputDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrlSearchOutputDirActionPerformed(evt);
            }
        });

        ctrlStart.setText(bundle.getString("StartButton")); // NOI18N
        ctrlStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrlStartActionPerformed(evt);
            }
        });

        ctrlOk.setText(bundle.getString("OkButton")); // NOI18N
        ctrlOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrlOkActionPerformed(evt);
            }
        });

        ctrlRepository.setEditable(true);
        ctrlRepository.setModel(new DefaultComboBoxModel<String>());
        ctrlRepository.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrlRepositoryActionPerformed(evt);
            }
        });

        jLabel4.setText(bundle.getString("lblBranch")); // NOI18N

        ctrlBranch.setModel(new DefaultComboBoxModel<String>());

        jLabel5.setText("Full version:");

        jMenu1.setText(bundle.getString("GitPackages.jMenu2.text")); // NOI18N

        jMenuAbout.setText(bundle.getString("AboutMenu")); // NOI18N
        jMenuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAboutActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuAbout);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ctrlStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ctrlOk))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(ctrlIssueId)
                                .addGap(70, 70, 70))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ctrlZipFilePrefix, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ctrlRepository, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ctrlBranch, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ctrlSearchRepository, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ctrlSearchOutputDir, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ctrlFullVersion)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(ctrlRepository, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ctrlSearchRepository, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(ctrlBranch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctrlIssueId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ctrlZipFilePrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(ctrlSearchOutputDir, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(ctrlFullVersion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctrlStart)
                    .addComponent(ctrlOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ctrlSearchRepositoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrlSearchRepositoryActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select repository directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (ctrlRepository.getSelectedIndex() != -1) {
            chooser.setCurrentDirectory(new File((String)ctrlRepository.getSelectedItem()));
        }
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            ctrlRepository.addItem(chooser.getSelectedFile().getAbsolutePath());
            ctrlRepository.setSelectedIndex(ctrlRepository.getItemCount()-1);
        }
    }//GEN-LAST:event_ctrlSearchRepositoryActionPerformed

    private void ctrlSearchOutputDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrlSearchOutputDirActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select target directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File currDir = new File(ctrlZipFilePrefix.getText());
        
        /// maybe its not a valid file, so we need to get the alid part.
        if (!currDir.exists()) {
          currDir = new File(currDir.getParent());
        }
        
        chooser.setCurrentDirectory(currDir);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            ctrlZipFilePrefix.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_ctrlSearchOutputDirActionPerformed

    private void ctrlStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrlStartActionPerformed
        ArrayList<String> commits = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();
        String wrkDir = (String)(ctrlRepository.getSelectedItem());
        String outZipPath = wrkDir;
        String outZipname = ctrlZipFilePrefix.getText()+"-"+ctrlIssueId.getText()+".zip";
        String outFullZipname = ctrlZipFilePrefix.getText()+".zip";
        DefaultListModel<String> model = (DefaultListModel<String>)(ctrlMessages.getModel());
        model.addElement(outZipname);
        model.addElement("");

        try {
            try {
                ResultSet rs = Repo.fetchByNeve(wrkDir);
                Repo adat = null;
                if (rs.next()) {
                    adat = new Repo(rs);
                    adat.setIssueId(ctrlIssueId.getText());
                    adat.setZipPrefix(ctrlZipFilePrefix.getText());
                    adat.setBranch((String)ctrlBranch.getSelectedItem());
                    int nr = 0;
                    nr = adat.update();
                } else {
                    adat = new Repo(-1, wrkDir);
                    adat.setIssueId(ctrlIssueId.getText());
                    adat.setZipPrefix(ctrlZipFilePrefix.getText());
                    adat.setBranch((String)ctrlBranch.getSelectedItem());
                    int nr = 0;
                    nr = adat.insert();
                }
            } catch (SQLException ex) {
                model.addElement(ex.toString());
            }
            GitRepo repo = new GitRepo(wrkDir);
            if (repo.getRepository().isBare()) {
                model.addElement("git repo is bare.");
            }

            model.addElement("Comments");
            ZipOutputStream zar = new ZipOutputStream(new FileOutputStream(outZipname));
            ZipOutputStream zarFull = null;
            if (ctrlFullVersion.isSelected()) {
                zarFull = new ZipOutputStream(new FileOutputStream(outFullZipname));
            }
            
            HashMap<String, RepoEntry> sumfileList = new HashMap<>();
            int lastTimeStamp = -1;

            RevCommit lastCommit = null;
            String branch = (String)ctrlBranch.getSelectedItem();
            HashMap<String, RevCommit> comments = repo.getComments(ctrlIssueId.getText(), branch);
            Iterator it = comments.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                RevCommit commit = comments.get(key);
                model.addElement(" ");
                Date commitdate = new Date(commit.getCommitTime()*1000L);
                DateFormat df = DateFormat.getDateTimeInstance();
                model.addElement(df.format(commitdate));
                model.addElement(commit.getFullMessage());
                String zipdirpref = new Integer(commit.getCommitTime()).toString();

                HashMap<ObjectId, String> fileList = repo.getFilelistForComment(commit);
                for (ObjectId fname : fileList.keySet()) {
                    //byte[] tartalom = repo.open(fname);
                    // zar.putNextEntry(new ZipEntry(zipdirpref+File.separator+fileList.get(fname)));
                    //zar.write(tartalom, 0, tartalom.length);
                    //zar.closeEntry();
                    if (lastTimeStamp < commit.getCommitTime()) {
                        //model.addElement("*"+zipdirpref+File.separator+fileList.get(fname));
                        RepoEntry entry = new RepoEntry(fileList.get(fname), 
                                fname, 
                                commit.getCommitTime());
                        sumfileList.put(fileList.get(fname), entry);
                    } else {
                        if (!sumfileList.containsKey(fileList.get(fname))) {
                            //model.addElement("+"+zipdirpref+File.separator+fileList.get(fname));
                            RepoEntry entry = new RepoEntry(fileList.get(fname), 
                                    fname, 
                                    commit.getCommitTime());
                            sumfileList.put(fileList.get(fname), entry);
                        } else {
                          RepoEntry entry = sumfileList.get(fileList.get(fname));
                          if (entry.getCommitTime() < commit.getCommitTime()) {
                            entry = new RepoEntry(fileList.get(fname), 
                                    fname, 
                                    commit.getCommitTime());
                            sumfileList.put(fileList.get(fname), entry);
                            //model.addElement("*"+zipdirpref+File.separator+fileList.get(fname));
//                          } else {
//                            model.addElement("-"+zipdirpref+File.separator+fileList.get(fname));
                          }
                        }
                    }
                }
                if (lastCommit == null) {
                    lastTimeStamp = commit.getCommitTime();
                    lastCommit = commit;
                } else {
                    if (commit.getCommitTime() > lastCommit.getCommitTime()) {
                        lastCommit = commit;
                        lastTimeStamp = commit.getCommitTime();
                    }
                }
            }
            model.addElement("Files:");
            for (String fname : sumfileList.keySet()) {
                model.addElement(fname);
                byte[] tartalom = repo.open(sumfileList.get(fname).getObjectId());
                zar.putNextEntry(new ZipEntry(fname));
                zar.write(tartalom, 0, tartalom.length);
                zar.closeEntry();
            }
            zar.close();
            if (zarFull != null) {
                TreeWalk treeWalk = new TreeWalk(repo.getRepository());
                treeWalk.addTree(lastCommit.getTree());
                treeWalk.setRecursive(true);
                model.addElement("Full version Files:");

                while(treeWalk.next()){
                    if (!treeWalk.getPathString().contains(".gitignore")) {
                        model.addElement(treeWalk.getPathString());
                        
                        ObjectId objId = treeWalk.getObjectId(0);
                        if (objId == null) {
                            model.addElement("not resolved");
                        } else {
                            byte[] tartalom = repo.open(objId);
                            zarFull.putNextEntry(new ZipEntry(treeWalk.getPathString()));
                            zarFull.write(tartalom, 0, tartalom.length);
                            zarFull.closeEntry();
                        }
                    }
                }  
                zarFull.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            model.addElement(ex.toString());
        }
    }//GEN-LAST:event_ctrlStartActionPerformed

    private void ctrlOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrlOkActionPerformed
        // TODO add your handling code here:
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_ctrlOkActionPerformed

    private void ctrlRepositoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrlRepositoryActionPerformed
      // TODO add your handling code here:
      DefaultListModel<String> model = (DefaultListModel<String>) ctrlMessages.getModel();
      try {
        String wrkDir = (String)ctrlRepository.getSelectedItem();
        ResultSet rs = Repo.fetchByNeve(wrkDir);
        String branch = new String();
        while (rs.next()) {
            Repo adat = new Repo(rs);
            ctrlIssueId.setText(adat.getIssueId());
            ctrlZipFilePrefix.setText(adat.getZipPrefix());
            branch = adat.getBranch();
        }
        DefaultComboBoxModel<String> cmodel = (DefaultComboBoxModel<String>)(ctrlBranch.getModel());
        cmodel.removeAllElements();
        GitRepo repo = new GitRepo(wrkDir);
        HashMap<ObjectId, String> branches = repo.getBranches();
        Iterator it = branches.values().iterator();
        while (it.hasNext()) {
          String br = (String)it.next();
          cmodel.addElement(br);
        }
        if (branch != null && !branch.isEmpty()) {
            cmodel.setSelectedItem(branch);
        }
        
      } catch (SQLException ex) {
        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
      }
    }//GEN-LAST:event_ctrlRepositoryActionPerformed

    private void jMenuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAboutActionPerformed
        // TODO add your handling code here:
        AboutDialog dlg = new AboutDialog(this, true);
        dlg.setVisible(true);
    }//GEN-LAST:event_jMenuAboutActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                frame.Init();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox ctrlBranch;
    private javax.swing.JCheckBox ctrlFullVersion;
    private javax.swing.JTextField ctrlIssueId;
    private javax.swing.JList ctrlMessages;
    private javax.swing.JButton ctrlOk;
    private javax.swing.JComboBox ctrlRepository;
    private javax.swing.JButton ctrlSearchOutputDir;
    private javax.swing.JButton ctrlSearchRepository;
    private javax.swing.JButton ctrlStart;
    private javax.swing.JTextField ctrlZipFilePrefix;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuAbout;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
