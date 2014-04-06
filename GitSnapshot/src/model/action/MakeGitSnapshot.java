/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.action;

import git.GitRepo;
import gui.MainFrame;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javafx.application.Platform;
import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;
import model.Repo;
import model.RepoEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

/**
 *
 * @author U292156
 */
public class MakeGitSnapshot extends javafx.concurrent.Task<String> {
    private javafx.scene.control.ComboBox ctrlRepository;
    private javafx.scene.control.TextField ctrlZipFilePrefix;
    private javafx.scene.control.TextField ctrlIssueId;
    private javafx.scene.control.ComboBox ctrlBranch;
    private javafx.scene.control.CheckBox ctrlFullVersion;
    private javafx.scene.control.TextArea ctrlLogWin;
    
    public MakeGitSnapshot(javafx.scene.control.ComboBox ctrlRepository,
            javafx.scene.control.TextField ctrlZipFilePrefix,
            javafx.scene.control.TextField ctrlIssueId,
            javafx.scene.control.ComboBox ctrlBranch,
            javafx.scene.control.CheckBox ctrlFullVersion,
            javafx.scene.control.TextArea ctrlLogWin) {
        this.ctrlRepository = ctrlRepository;
        this.ctrlZipFilePrefix = ctrlZipFilePrefix;
        this.ctrlIssueId = ctrlIssueId;
        this.ctrlBranch = ctrlBranch;
        this.ctrlFullVersion = ctrlFullVersion;
        this.ctrlLogWin = ctrlLogWin;
    }

    @Override
    protected String call() throws Exception {
        ArrayList<String> commits = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();
        String wrkDir = (String)(ctrlRepository.getValue());
        String outZipPath = wrkDir;
        String outZipname = ctrlZipFilePrefix.getText()+"-"+ctrlIssueId.getText()+".zip";
        publish(outZipname);

        try {
            try {
                ResultSet rs = Repo.fetchByNeve(wrkDir);
                Repo adat = null;
                if (rs.next()) {
                    adat = new Repo(rs);
                    adat.setIssueId(ctrlIssueId.getText());
                    adat.setZipPrefix(ctrlZipFilePrefix.getText());
                    adat.setBranch((String)ctrlBranch.getValue());
                    int nr = 0;
                    nr = adat.update();
                } else {
                    adat = new Repo(-1, wrkDir);
                    adat.setIssueId(ctrlIssueId.getText());
                    adat.setZipPrefix(ctrlZipFilePrefix.getText());
                    adat.setBranch((String)ctrlBranch.getValue());
                    int nr = 0;
                    nr = adat.insert();
                }
            } catch (SQLException ex) {
                publish(ex.toString());
            }
            GitRepo repo = new GitRepo(wrkDir);
            if (repo.getRepository().isBare()) {
                publish("git repo is bare.");
            }

            publish("Comments");
            ZipOutputStream zar = new ZipOutputStream(new FileOutputStream(outZipname));
            
            HashMap<String, RepoEntry> sumfileList = new HashMap<>();
            int lastTimeStamp = -1;

            RevCommit lastCommit = null;
            String branch = (String)ctrlBranch.getValue();
            HashMap<String, RevCommit> comments = repo.getComments(ctrlIssueId.getText(), branch);
            Iterator it = comments.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                RevCommit commit = comments.get(key);
                publish("\n");
                Date commitdate = new Date(commit.getCommitTime()*1000L);
                DateFormat df = DateFormat.getDateTimeInstance();
                publish(df.format(commitdate));
                publish(commit.getFullMessage());
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
            publish("Files:");
            if (ctrlFullVersion.isSelected()) {
                TreeWalk treeWalk = new TreeWalk(repo.getRepository());
                treeWalk.addTree(lastCommit.getTree());
                treeWalk.setRecursive(true);
                publish("Full version Files:");

                while(treeWalk.next()){
                    if (!treeWalk.getPathString().contains(".gitignore")) {
                        publish(treeWalk.getPathString());
                        
                        ObjectId objId = treeWalk.getObjectId(0);
                        if (objId == null) {
                            publish("not resolved");
                        } else {
                            byte[] tartalom = repo.open(objId);
                            zar.putNextEntry(new ZipEntry(treeWalk.getPathString()));
                            zar.write(tartalom, 0, tartalom.length);
                            zar.closeEntry();
                        }
                    }

                }  
            } else {
                for (String fname : sumfileList.keySet()) {
                    publish(fname);
                    byte[] tartalom = repo.open(sumfileList.get(fname).getObjectId());
                    zar.putNextEntry(new ZipEntry(fname));
                    zar.write(tartalom, 0, tartalom.length);
                    zar.closeEntry();
                }
            }
            zar.setComment(lastCommit.getFullMessage());
            zar.close();
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            publish(ex.toString());
        }
        return (outZipname);
    }

    protected void publish(final String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ctrlLogWin.appendText(message+"\n");                                    
            }
        }); 
    }

}
