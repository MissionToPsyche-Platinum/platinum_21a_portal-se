from pathlib import Path
import shutil
import subprocess
import sys

ROOT = Path(__file__).resolve().parent
WORKTREE_DIR = ROOT / ".tmp_worktree"

DEPLOY_BRANCH = "us74-deploy"

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


# Copy flattened file structure to temp worktree

# commit changes

# build and deploy


def main():
    #Test code
    result = run(["git", "status"]).stdout.strip()
    print(result)

    print("creating worktree")
    create_worktree()
    worktree_result = run(["git", "branch"], str(WORKTREE_DIR)).stdout.strip()
    print(worktree_result)
    worktree_list = run(["git", "worktree", "list"]).stdout.strip()
    print("git list of worktrees: ", worktree_list)

    print("destroying worktree")
    destroy_worktree()
    print("Worktree destroyed: ", not WORKTREE_DIR.exists())
    worktree_list = run(["git", "worktree", "list"]).stdout.strip()
    print("git list of worktrees: ", worktree_list)


if __name__ == "__main__":
    main()
