package com.lhsystems.jgit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ReflogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.ReflogEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

public class Client {

	private static String gitRepo = "C:\\work\\GIT\\netlineload";
	private Git git;

	private Git openRepo(String gitworkDir) {
		try {
			File dir = new File(gitworkDir);
			if (git == null
					|| !git.getRepository().getDirectory().getAbsoluteFile()
							.equals(gitRepo)) {
				git = Git.open(dir);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return git;
	}

	public Collection<String> getComments() {
		Collection<String> ret = new ArrayList<String>();
		Map<String, Ref> refs = git.getRepository().getAllRefs();
		return refs.keySet();
	}

	private Collection<ReflogEntry> reflogs(Git git) {
		ReflogCommand reflog = git.reflog();
		Collection<ReflogEntry> reflogs;
		try {
			reflogs = reflog.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return reflogs;
	}

	public Collection<String> getFilelistForComment(String comment)
			throws RevisionSyntaxException, AmbiguousObjectException,
			IncorrectObjectTypeException, IOException {
		Collection<String> ret = new ArrayList<String>();
		ObjectId commitId = git.getRepository().resolve(comment);
		RevCommit commit = getCommit(git.getRepository(), comment);
		TreeWalk treeWalk = new TreeWalk(git.getRepository());
		treeWalk.setRecursive(true);
		treeWalk.addTree(commit.getTree());
		while (treeWalk.next()) {
			treeWalk.getRawPath();
			ret.add(treeWalk.getPathString());
		}
		treeWalk.release();
		return ret;
	}

	public Collection<String> getBranches() {
		Collection<String> ret = new ArrayList<String>();
		return ret;
	}

	public static RevCommit getCommit(Repository repository, String revCommit)
			throws RevisionSyntaxException, AmbiguousObjectException,
			IncorrectObjectTypeException, IOException {
		RevCommit commit = null;
		// resolve object id
		ObjectId branchObject = repository.resolve(revCommit);
		RevWalk walk = new RevWalk(repository);
		RevCommit rev = walk.parseCommit(branchObject);
		commit = rev;
		walk.dispose();
		return commit;
	}

	public Collection<String> getDiffFileList(String logEntry)
			throws MissingObjectException, IncorrectObjectTypeException,
			IOException {
		for (ReflogEntry reflogEntry : this.reflogs(git)) {
			try {
				if (reflogEntry.getComment().equals(logEntry)) {
					return getDiffFileList(logEntry);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public Collection<String> getDiffFileList(ReflogEntry logEntry)
			throws MissingObjectException, IncorrectObjectTypeException,
			IOException {
		final Repository repo = git.getRepository();
		RevWalk rw = new RevWalk(repo);
		final Collection<String> ret = new ArrayList<String>();
		RevCommit revCommit = rw.parseCommit(logEntry.getNewId());
		RevCommit parentCommit = null;

		if (revCommit.getParent(0) != null) {
			try {
				parentCommit = rw.parseCommit(revCommit.getParent(0).getId());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
		df.setRepository(repo);
		df.setDiffComparator(RawTextComparator.DEFAULT);
		df.setDetectRenames(true);

		List<DiffEntry> diffs = null;
		try {
			diffs = df.scan(parentCommit.getTree(), revCommit.getTree());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (DiffEntry diff : diffs) {
			ret.add(diff.getNewPath());
		}
		return ret;
	}

	public static void main(String[] args) {
		Client client = new Client();
		final Git git = client.openRepo(gitRepo);
		final Repository repo = git.getRepository();
		git.pull();
		RevWalk rw = new RevWalk(repo);

		for (ReflogEntry reflogEntry : client.reflogs(git)) {
			try {
				RevCommit revCommit = rw.parseCommit(reflogEntry.getNewId());
				System.out.println(revCommit.getShortMessage());
				for (String res : client.getDiffFileList(reflogEntry)) {
					System.out.println("  " + res);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
