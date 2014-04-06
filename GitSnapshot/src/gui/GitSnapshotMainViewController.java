package gui;

import git.GitRepo;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.swing.DefaultComboBoxModel;
import model.Repo;
import model.action.MakeGitSnapshot;
import org.eclipse.jgit.lib.ObjectId;


public class GitSnapshotMainViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem ctrlAbout;

    @FXML
    private ComboBox<String> ctrlBranch;

    @FXML
    private Button ctrlClose;

    @FXML
    private CheckBox ctrlFullVersion;

    @FXML
    private TextField ctrlIssueId;

    @FXML
    private TextArea ctrlLogWin;

    @FXML
    private ComboBox<String> ctrlRepository;

    @FXML
    private Button ctrlSearchRepository;

    @FXML
    private Button ctrlSearchZipLocation;

    @FXML
    private Button ctrlStart;

    @FXML
    private TextField ctrlZipFilePrefix;


    @FXML
    void OnCloseButton(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void OnSearchRepository(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select repository directory");
        if (ctrlRepository.getValue() != null 
            && !ctrlRepository.getValue().isEmpty()) {
            File initDir = new File(ctrlRepository.getValue());
            chooser.setInitialDirectory(initDir);
        }
        File returnVal = chooser.showDialog(null);
        if(returnVal != null) {
            ObservableList<String> repoList = ctrlRepository.getItems();
            if (ctrlRepository.getItems().isEmpty()) {
                repoList = FXCollections.observableArrayList();
            }
            String ujRepo = returnVal.getAbsolutePath();
            repoList.addAll(ujRepo);
            
            ctrlRepository.setItems(repoList);
            ctrlRepository.setValue(ujRepo);
        }
    }

    @FXML
    void OnStartButton(ActionEvent event) {
        ctrlLogWin.setText("");
        MakeGitSnapshot mk = new MakeGitSnapshot(  ctrlRepository,
                                                   ctrlZipFilePrefix,
                                                   ctrlIssueId,
                                                   ctrlBranch,
                                                   ctrlFullVersion,
                                                   ctrlLogWin);
        //ctrlLogWin.textProperty().bind(mk.messageProperty());
        new Thread(mk).start();
    }

    @FXML
    void onAbout(ActionEvent event) {
    }

    @FXML
    void onBranchChanged(ActionEvent event) {
    }

    @FXML
    void onRepositoryChanged(ActionEvent event) {
      // TODO add your handling code here:
      try {
        String wrkDir = (String)ctrlRepository.getValue();
        ResultSet rs = Repo.fetchByNeve(wrkDir);
        String branch = new String();
        ObservableList<String> branchList = ctrlBranch.getItems();
        if (branchList.isEmpty()) {
            branchList = FXCollections.observableArrayList();
        }
        while (rs.next()) {
            Repo adat = new Repo(rs);
            ctrlIssueId.setText(adat.getIssueId());
            ctrlZipFilePrefix.setText(adat.getZipPrefix());
            branch = adat.getBranch();
        }
        GitRepo repo = new GitRepo(wrkDir);
        HashMap<ObjectId, String> branches = repo.getBranches();
        Iterator it = branches.values().iterator();
        while (it.hasNext()) {
          String br = (String)it.next();
          branchList.add(br);
        }
        if (branch != null && !branch.isEmpty()) {
            ctrlBranch.setValue(branch);
        }
        ctrlBranch.setItems(branchList);
      } catch (SQLException ex) {
        Logger.getLogger(GitSnapshotMain.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(GitSnapshotMain.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    @FXML
    void onZipFilePrefixSearch(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select target directory");
        String currPrefix = ctrlZipFilePrefix.getText();
        File currDir = new File(currPrefix);
        
        /// maybe its not a valid file, so we need to get the alid part.
        if (!currPrefix.isEmpty() && !currDir.exists()) {
          currDir = new File(currDir.getParent());
        }
        
        if (currDir.exists()) {
            chooser.setInitialDirectory(currDir);
        }
        File returnVal = chooser.showOpenDialog(null);
        if(returnVal != null) {
            String ujRepo = returnVal.getAbsolutePath();
            ctrlZipFilePrefix.setText(ujRepo);
        }
    }

    @FXML
    void initialize() {
        assert ctrlAbout != null : "fx:id=\"ctrlAbout\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";
        assert ctrlBranch != null : "fx:id=\"ctrlBranch\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";
        assert ctrlClose != null : "fx:id=\"ctrlClose\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";
        assert ctrlFullVersion != null : "fx:id=\"ctrlFullVersion\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";
        assert ctrlIssueId != null : "fx:id=\"ctrlIssueId\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";
        assert ctrlLogWin != null : "fx:id=\"ctrlLogWin\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";
        assert ctrlRepository != null : "fx:id=\"ctrlRepository\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";
        assert ctrlSearchRepository != null : "fx:id=\"ctrlSearchRepository\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";
        assert ctrlSearchZipLocation != null : "fx:id=\"ctrlSearchZipLocation\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";
        assert ctrlStart != null : "fx:id=\"ctrlStart\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";
        assert ctrlZipFilePrefix != null : "fx:id=\"ctrlZipFilePrefix\" was not injected: check your FXML file 'GitSnapshotMainView.fxml'.";

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            ctrlLogWin.appendText(ex.toString());
            ctrlLogWin.appendText("\n");
        }        
        ctrlRepository.getItems().clear();
        try {
          ResultSet rs = Repo.fetchAll();
          String wrkDir = new String();
          ObservableList<String> repoList = FXCollections.observableArrayList();
          
          while (rs.next()) {
            Repo adat = new Repo(rs);
            repoList.add(adat.getNeve());
            if (wrkDir.isEmpty()) {
               wrkDir = adat.getNeve();
            }
          }
          ctrlRepository.setItems(repoList);
        } catch (SQLException ex) {
          Logger.getLogger(GitSnapshotMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
