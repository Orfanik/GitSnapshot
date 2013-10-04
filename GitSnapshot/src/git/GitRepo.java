
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package git;

//~--- non-JDK imports --------------------------------------------------------

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

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
    HashMap<String, String> comments = new HashMap<String, String>();
    
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
              comments.put(commit.getId().getName(), commit.getFullMessage());
            }
        }    
    } catch (GitAPIException ex) {
        Logger.getLogger(GitRepo.class.getName()).log(Level.SEVERE, null, ex);
    }
    return comments;
  }
  
}


//~ Formatted by Jindent --- http://www.jindent.com
