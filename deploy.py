from pathlib import Path
import shutil
import subprocess
import sys

ROOT = Path(__file__).resolve().parent
SOURCE_DIR = ROOT / "Psyche-mission"
WORKTREE_DIR = ROOT / ".tmp_worktree"

DEPLOY_BRANCH = "us74-deploy"

IGNORED = {".git", ".tmp_worktree", "node_modules", "dist", "deploy.py"}

# function for running git commands
def run(command, curr_directory=None):
    cwd_str = str(Path(curr_directory).resolve()) if curr_directory else None

    result = subprocess.run(command, cwd=cwd_str, text=True, capture_output=True, shell=False)

    return result


# create temporary worktree directory
def create_worktree():
    # alerts to uncommitted changes to current branch
    # branch_status = run(["git", "status", "--porcelain"]).stdout.strip()
    # if branch_status:
    #     print("Uncommitted changes in this branch. Please commit changes first")
    #     sys.exit(1)    

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

# build and deploy


def test():
    #Test commands
    print("running git status command...")
    result = run(["git", "status"]).stdout.strip()
    print(result)

    # Test creating worktree
    print("creating worktree...")
    create_worktree()
    worktree_result = run(["git", "branch"], str(WORKTREE_DIR)).stdout.strip()
    print(worktree_result)
    worktree_list = run(["git", "worktree", "list"]).stdout.strip()
    print("git list of worktrees: ", worktree_list)

    # Test removing items from worktree
    print("Cleaning worktree...")
    clean_worktree()
    cleaning_result = run(["cmd", "/c", "dir", "/a", "/b"], str(WORKTREE_DIR)).stdout.strip()
    print(f"Files remaining in Worktree: {cleaning_result}")

    # Test copying items to worktree
    print("Copying source to worktree...")
    copy_to_worktree()
    copy_result = run(["cmd", "/c", "dir", "/a", "/b"], str(WORKTREE_DIR)).stdout.strip()
    print(f"Files in Worktree: {copy_result}")

    # Test deleting worktree
    print("destroying worktree...")
    destroy_worktree()
    print("Worktree destroyed: ", not WORKTREE_DIR.exists())
    worktree_list = run(["git", "worktree", "list"]).stdout.strip()
    print("git list of worktrees: ", worktree_list)


def main():
    pass


if __name__ == "__main__":
    test()
    # main()
