package com.lhsystems.jgit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ReflogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.ReflogEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

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

	private Collection<ReflogEntry> reflog(Git git) {
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

	public static RevCommit getCommit(Repository repository, String objectId)
			throws RevisionSyntaxException, AmbiguousObjectException,
			IncorrectObjectTypeException, IOException {
		RevCommit commit = null;
		// resolve object id
		ObjectId branchObject;
		branchObject = repository.resolve(objectId);
		RevWalk walk = new RevWalk(repository);
		RevCommit rev = walk.parseCommit(branchObject);
		commit = rev;
		walk.dispose();
		return commit;
	}

	private Iterator<RevCommit> getLogsIterable(Git git)
			throws NoHeadException, GitAPIException {
		Iterable<RevCommit> log;
		log = git.log().call();
		return log.iterator();
	}

	public static void main(String[] args) {
		Client client = new Client();
		final Git git = client.openRepo(gitRepo);
		git.pull();
		for (String key : client.getComments()) {
			if (!key.endsWith("HEAD")) {
				System.out.println("Comment:");
				System.out.println("  " + key);
				System.out.println(" Files:");
				try {
					for (String file : client.getFilelistForComment(key)) {
						System.out.println("  " + file);
					}
				} catch (RevisionSyntaxException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
