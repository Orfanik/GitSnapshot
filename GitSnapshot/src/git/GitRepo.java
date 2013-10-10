
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package git;

//~--- non-JDK imports --------------------------------------------------------

import java.io.BufferedReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectStream;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

/**
 *
 * @author U292156
 */
public class GitRepo {
  String     repoUrl    = null;
  Repository repository = null;

  public GitRepo(String repoName) throws IOException {
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    repository = builder.setGitDir(new File(repoName+"/.git")).readEnvironment()    // scan environment GIT_* variables
      .findGitDir()                                                                    // scan up the file system tree
      .build();
  }

  /**
   *
   * @param object
   */
  public ArrayList<String> open(ObjectId object) throws MissingObjectException, IOException {
      ObjectLoader loader = repository.open(object);
      ArrayList<String> tartalom = new ArrayList<>();
      
      ObjectStream ost = loader.openStream();  
      BufferedReader reader = new BufferedReader( new InputStreamReader( ost ) );   
      for(String line = reader.readLine(); line != null; line = reader.readLine()) {
        tartalom.add(line);
      }
      return tartalom;
      
  }
  
  public ArrayList getBranches() {
    ArrayList<String> branches = new ArrayList<>();
    try {
        List<Ref> call = new Git(repository).branchList().call();
        for(Ref ref : call) {
            branches.add(ref.getName() + " : " + ref.getObjectId().getName());
        }
    } catch (GitAPIException ex) {
        Logger.getLogger(GitRepo.class.getName()).log(Level.SEVERE, null, ex);
    }
    return branches;
  }
  
  public HashMap getComments(String filterText) throws IOException {
    HashMap<String, RevCommit> comments = new HashMap<>();
    
    try {
        Git git = new Git(repository);
        Iterable<RevCommit> commits = git.log().all().call();
        for (RevCommit commit : commits) {
            boolean filterOk = true;
            if (!filterText.isEmpty()) {
              if (commit.getFullMessage().contains(filterText)) {
                  filterOk = true;
              } else {
                  filterOk = false;
              }
            }
            if (filterOk) {
              comments.put(commit.getId().getName(), commit);
            }
        }    
    } catch (GitAPIException ex) {
        Logger.getLogger(GitRepo.class.getName()).log(Level.SEVERE, null, ex);
    }
    return comments;
  }

  public HashMap<ObjectId, String> getFilelistForComment(RevCommit commit)
    throws RevisionSyntaxException, AmbiguousObjectException,
    IncorrectObjectTypeException, IOException {

    HashMap<ObjectId, String> files = new HashMap<>();
      
    if (commit.getParentCount() > 0) {
        RevCommit parentCommit = commit.getParent(0);
        DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
        df.setRepository(repository);
        df.setDiffComparator(RawTextComparator.DEFAULT);
        df.setDetectRenames(true);

        List<DiffEntry> diffs = null;
        try {
          diffs = df.scan(parentCommit.getTree(), commit.getTree());
        } catch (IOException e) {
                e.printStackTrace();
        }
        for (DiffEntry diff : diffs) {
            
            
//            ObjectLoader loader = repository.open(objectId);
            // and then one can the loader to read the file
//            loader.copyTo(System.out);               
          files.put((ObjectId)diff.getNewId().toObjectId(), diff.getNewPath());
        }
    }
    return files;
  }

  
}


//~ Formatted by Jindent --- http://www.jindent.com