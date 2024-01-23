# git log --pretty=format:"%ad"
from subprocess import Popen, PIPE

git_log = ["git", "log", "--pretty=format:%ad", "--date=format-local:%a-%H:%M"]
process = Popen(git_log, stdout=PIPE, stderr=PIPE, text=True)
out, err = process.communicate()

print(out)