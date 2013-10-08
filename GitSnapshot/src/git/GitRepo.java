
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
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
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

  public ArrayList<String> getFilelistForComment(RevCommit commit)
    throws RevisionSyntaxException, AmbiguousObjectException,
    IncorrectObjectTypeException, IOException {

    ArrayList<String> ret = new ArrayList<>();
      
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
          ret.add(diff.getNewPath());
        }
    }
    return ret;
  }

  
}


//~ Formatted by Jindent --- http://www.jindent.com
