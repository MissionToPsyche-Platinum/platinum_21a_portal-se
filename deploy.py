from pathlib import Path
import shutil
import subprocess
import sys

ROOT = Path(__file__).resolve().parent
SOURCE_DIR = ROOT / "Psyche-mission"
WORKTREE_DIR = ROOT / ".tmp_worktree"

DEPLOY_BRANCH = "us74-deploy"
PAGES_BRANCH = "gh-pages"

IGNORED = {".git", ".tmp_worktree", "node_modules", "dist", "deploy.py"}

# function for running git commands
def run(command, curr_directory=None, check=True, capture=True):
    cwd_str = str(Path(curr_directory).resolve()) if curr_directory else None

    result = subprocess.run(command, cwd=cwd_str, text=True, capture_output=capture, shell=True)

    if check and result.returncode != 0:
        if result.stderr:
            print(f"\n[ERROR] Command failed: {' '.join(command)}")
            print(f"Error Details:\n{result.stderr.strip()}")
        sys.exit(result.returncode)

    return result


# create temporary worktree directory
def create_worktree():
    # alerts to uncommitted changes to current branch
    # branch_status = run(["git", "status", "--porcelain"]).stdout.strip()
    # if branch_status:
    #     print("Uncommitted changes in this branch. Please commit changes first")
    #     sys.exit(1)    

    # if worktree already exists, destroy it
    destroy_worktree()

    # place deploy branch into worktree
    run(["git", "worktree", "add", str(WORKTREE_DIR), DEPLOY_BRANCH])


# destroy temporary worktree
def destroy_worktree():
    if WORKTREE_DIR.exists():
        run(["git", "worktree", "remove", "--force", str(WORKTREE_DIR)])
        shutil.rmtree(WORKTREE_DIR, ignore_errors=True)
        run(["git", "worktree", "prune"])


# remove all items from worktree but preserve .git
def clean_worktree():
        for item in WORKTREE_DIR.iterdir():
            if item.name == ".git":
                continue
            elif item.is_dir():
                shutil.rmtree(item)
            else:
                item.unlink()


# copy items from source directory to temp worktree
def copy_to_worktree():
    for item in SOURCE_DIR.iterdir():
        if item.name in IGNORED:
            continue
        destination = WORKTREE_DIR / item.name
        if item.is_dir():
            shutil.copytree(item, destination)
        else:
            shutil.copy2(item, destination)


# commit changes
def commit_worktree_changes():
    run(["git", "add", "-A"], WORKTREE_DIR)
    # check to see if anything has been staged
    diff = run(["git", "diff", "--cached", "--quiet"], WORKTREE_DIR, check=False)

    if diff.returncode != 0:
        run(["git", "commit", "-m", "Update deployment branch"], WORKTREE_DIR)
        run(["git", "push", "origin", DEPLOY_BRANCH], WORKTREE_DIR)
        print("Pushed deploy branch changes to remote repo")
    else:
        print("Deploy branch is already up to date.")


# build and deploy
def build_and_deploy():
    print("Installing dependencies in worktree...")
    run(["npm", "install"], WORKTREE_DIR)

    print("Building...")
    run(["npm", "run", "build"], WORKTREE_DIR)

    print("Deploying...")
    run(["npx", "--yes", "gh-pages", "-d", "../.tmp_worktree/dist", "-b", PAGES_BRANCH], SOURCE_DIR, capture=False)

    print("Deployed successfully")



def test():
    #Test commands
    print("running git status command...")
    result = run(["git", "status"]).stdout.strip()
    print(result)

    # Test creating worktree
    print("creating worktree...")
    create_worktree()
    worktree_result = run(["git", "branch"], WORKTREE_DIR).stdout.strip()
    print(worktree_result)
    worktree_list = run(["git", "worktree", "list"]).stdout.strip()
    print("git list of worktrees: ", worktree_list)

    # Test removing items from worktree
    print("Cleaning worktree...")
    clean_worktree()
    cleaning_result = run(["dir", "/a", "/b"], WORKTREE_DIR).stdout.strip()
    print(f"Files remaining in Worktree: {cleaning_result}")

    # Test copying items to worktree
    print("Copying source to worktree...")
    copy_to_worktree()
    copy_result = run(["dir", "/a", "/b"], WORKTREE_DIR).stdout.strip()
    print(f"Files in Worktree: {copy_result}")

    # Test deleting worktree
    print("destroying worktree...")
    destroy_worktree()
    print("Worktree destroyed: ", not WORKTREE_DIR.exists())
    worktree_list = run(["git", "worktree", "list"]).stdout.strip()
    print("git list of worktrees: ", worktree_list)


def main():
    print("Setting up worktree...")
    create_worktree()

    print("Updating worktree with latest changes...")
    clean_worktree()
    copy_to_worktree()

    print(f"Committing changes to {DEPLOY_BRANCH}")
    commit_worktree_changes()

    print(f"Building and deploying to {DEPLOY_BRANCH}")
    build_and_deploy()

    print("Destroying worktree")
    destroy_worktree()
    print("Process complete!")


if __name__ == "__main__":
    # test()
    main()
