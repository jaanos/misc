#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <dirent.h>
#include <errno.h>
#include <unistd.h>
#include <fcntl.h>
#include <time.h>
#include <signal.h>
#include <sys/stat.h>

// nekateri podatki o lupini
char shell_name[] = "mysh";
int shell_terminal;
int shell_pid;
int shell_pgid;

// status zadnjega izvedenega ukaza
int last_command_status = 0;
// privzeti nacin za stvaritev datotek/direktorijev
int default_file_mode = 00666;   // osmisko stevilo
// razhroscevalni nacin (izklop=0, vklop=1)
int debug = 0;

char ** trap;

#define MAX_PATH_LENGTH 512

// ************************** Obdelava ukazne vrstice ***************

// ukazna vrstica
#define MAX_COMMAND_LENGTH  1023
char command_line[MAX_COMMAND_LENGTH];

// sparsani argumenti ukazne vrstice
#define MAX_ARGUMENTS   63
int argument_count;
char* arguments[MAX_ARGUMENTS];

int read_command_line() {
    // read raw command line
    fgets(command_line, MAX_COMMAND_LENGTH, stdin);
    return parse_command_line(command_line);
}

int parse_command_line(char * cmd) {
    argument_count = 0;
    arguments[0] = NULL;
    // find all arguments and fill arguments array
    char * pos = cmd;
    char * arg_start;
    while (1) {
        // skip white space = find start of token
        while (isspace(*pos)) pos++;
        if (*pos == 0 || *pos == '\n') break;
        arg_start = pos;
        if (*arg_start == '"') {
            // skip all until " is found
            pos++;
            while (*pos != 0 && *pos != '\n' && *pos != '"') pos++;
            arg_start++;
            *pos = 0;
            pos++;
        } else if (*arg_start == '\'') {
            // skip all until ' is found
            pos++;
            while (*pos != 0 && *pos != '\n' && *pos != '\'') pos++;
            arg_start++;
            *pos = 0;
            pos++;
        } else {
            // skip !isspace = find end of token
            while (*pos != 0 && *pos != '\n' && !isspace(*pos)) pos++;
            *pos = 0;
            pos++;
         }
        // put pointer to argument into arguments array
        arguments[argument_count] = arg_start;
        if (debug) fprintf(stderr, "Argument %i = %s\n", argument_count, arguments[argument_count]);
        argument_count++;
    }
    int i = argument_count;
    while (i < MAX_ARGUMENTS)
        arguments[i++] = NULL;
    return argument_count;
}

void print_prompt() {
    printf("%s %s> ", shell_name, getcwd(NULL,0));
}

// ************************** Obdelava vgrajenih ukazov *************

#define CMD_HELP    0
#define CMD_EXIT    1
#define CMD_QUIT    2
#define CMD_BYE     3
#define CMD_ECHO    4
#define CMD_PRINT   5
#define CMD_STATUS  6
#define CMD_PID     7
#define CMD_PWD     8
#define CMD_CD      9
#define CMD_MKDIR   10
#define CMD_RMDIR   11
#define CMD_LINK    12
#define CMD_UNLINK  13
#define CMD_REMOVE  14
#define CMD_RENAME  15
#define CMD_COPY    16
#define CMD_CAT     17
#define CMD_DIR     18
#define CMD_LS      19
#define CMD_DATE    20
#define CMD_KILL    21
#define CMD_TRAP    22
#define CMD_DEBUG   23

char * builtins[] = {
(char*)CMD_HELP,    "help",     "izpis kratke pomoci",
(char*)CMD_EXIT,    "exit",     "izhod iz lupine s podanim statusom",
(char*)CMD_QUIT,    "quit",     "izhod iz lupine s statusom zadnjega ukaza",
(char*)CMD_BYE,     "bye",      "izhod iz lupine s statusom 0 (true)",
(char*)CMD_ECHO,    "echo",     "izpis argumentov in skoka v novo vrstico",
(char*)CMD_PRINT,   "print",    "izpis argumentov",
(char*)CMD_STATUS,  "status",   "izpis statusa zadnjega ukaza",
(char*)CMD_PID,     "pid",      "izpis PIDa lupine",
(char*)CMD_PWD,     "pwd",      "izpis trenutnega delovnega direktorija",
(char*)CMD_CD,      "cd",       "sprememba delovnega direktorija",
(char*)CMD_MKDIR,   "mkdir",    "stvaritev novega direktorija",
(char*)CMD_RMDIR,   "rmdir",    "odstranitev podanega direktorija",
(char*)CMD_LINK,    "link",     "ustvarjanje povezave (linka)",
(char*)CMD_UNLINK,  "unlink",   "odstranjevanje datoteke (linka)",
(char*)CMD_REMOVE,  "remove",   "odstranjevanje datoteke ali direktorija",
(char*)CMD_RENAME,  "rename",   "preimenovanje datoteke ali direktorija",
(char*)CMD_COPY,    "copy",     "kopiranje datoteke",
(char*)CMD_CAT,     "cat",      "izpis vsebine datotek",
(char*)CMD_DIR,     "dir",      "izpis vsebine direktorija v stolpcu",
(char*)CMD_LS,      "ls",       "izpis vsebine direktorija",
(char*)CMD_DATE,    "date",     "izpis datuma in casa",
(char*)CMD_KILL,    "kill",     "posiljanje signala",
(char*)CMD_TRAP,    "trap",     "nastavitev pasti",
(char*)CMD_DEBUG,   "debug",    "zamenja razhroscevalni nacin",
(char*)NULL,        NULL,       NULL
};

int is_builtin(const char * cmd) {
    int i = 1;
    long ret;
    while (builtins[i] != NULL) {
        if (strcmp(builtins[i], cmd) == 0) return (long)builtins[i-1];
        i += 3;
    }
    return -1;
}

// ************************** Implementacija vgrajenih ukazov *******

int do_notimplemented() {
    fprintf(stderr, "%s: Neimplementirano.\n", arguments[0]);
    return 1;
}

int do_help() {
    int i;
    for (i = 1; builtins[i] != NULL; i += 3)
        printf("%10s - %s\n", builtins[i], builtins[i+1]);
    return 0;
}

int do_print() {
    int i = 1;
    while (arguments[i] != NULL) {
        printf("%s ",arguments[i++]);
    }
    return 0;
}

int do_echo() {
    int ret = do_print();
    putchar('\n');
    return ret;
}

int do_pwd() {
    printf("%s\n",getcwd(NULL,0));
    return 0;
}

int do_cd() {
    if (argument_count < 2) {
        printf("Uporaba: cd direktorij\n");
        return 1;
    }

    if (chdir(arguments[1]) == -1) {
        perror("cd");
        return errno;
    }
    return 0;
}

int do_mkdir() {
    if (argument_count < 2) {
        printf("Uporaba: mkdir direktorij\n");
        return 1;
    }

    if (mkdir(arguments[1],default_file_mode) == -1) {
        perror("mkdir");
        return errno;
    }
    return 0;
}

int do_rmdir() {
    if (argument_count < 2) {
        printf("Uporaba: rmdir direktorij\n");
        return 1;
    }

    if (rmdir(arguments[1]) == -1) {
        perror("rmdir");
        return errno;
    }
}

int do_link() {
    if (argument_count < 3) {
        printf("Uporaba: link datoteka povezava\n");
        return 1;
    }

    if (link(arguments[1],arguments[2]) == -1) {
        perror("link");
        return errno;
    }
    return 0;
}

int do_unlink() {
    if (argument_count < 2) {
        printf("Uporaba: unlink povezava\n");
        return 1;
    }

    if (unlink(arguments[1]) == -1) {
        perror("unlink");
        return errno;
    }
    return 0;
}

int do_remove() {
    if (argument_count < 2) {
        printf("Uporaba: remove datoteka\n");
        return 1;
    }

    if (remove(arguments[1]) == -1) {
        perror("remove");
        return errno;
    }
    return 0;
}

int do_rename() {
    if (argument_count < 3) {
        printf("Uporaba: rename izvor cilj\n");
        return 1;
    }

    if (rename(arguments[1],arguments[2]) == -1) {
        perror("rename");
        return errno;
    }
    return 0;
}

int do_copy() {
    int from, to, cnt, ret;

    if (argument_count < 3) {
        printf("Uporaba: copy izvor cilj\n");
        return 1;
    }

    char buf[MAX_COMMAND_LENGTH];
    if ((from = open(arguments[1],O_RDONLY)) == -1) {
        perror("copy");
        return errno;
    }
    if ((to = open(arguments[2],O_CREAT|O_TRUNC|O_WRONLY)) == -1) {
        perror("copy");
        ret = errno;
        if (close(from) == -1) {
            perror("copy");
        }
        return ret;
    }
    while ((cnt = read(from,buf,MAX_COMMAND_LENGTH)) != 0) {
        if (cnt == -1 || write(to,buf,cnt) == -1) {
            perror("copy");
            ret = errno;
            if (close(from) == -1) {
                perror("copy");
            }
            if (close(to) == -1) {
                perror("copy");
            }
            return ret;
        }
    }

    if (close(from) == -1) {
        perror("copy");
        ret = errno;
        if (close(to) == -1) {
            perror("copy");
        }
        return ret;
    }

    if (close(to) == -1) {
        perror("copy");
        return errno;
    }
    
    return 0;
}


int do_cat() {
    int from, cnt, ret;

    if (argument_count < 2) {
        printf("Uporaba: cat datoteka\n");
        return 1;
    }

    char buf[MAX_COMMAND_LENGTH];
    if ((from = open(arguments[1],O_RDONLY)) == -1) {
        perror("cat");
        return errno;
    }
    while ((cnt = read(from,buf,MAX_COMMAND_LENGTH)) != 0) {
        if (cnt == -1 || write(1,buf,cnt) == -1) {
            perror("cat");
            ret = errno;
            if (close(from) == -1) {
                perror("cat");
            }
            return ret;
        }
    }

    if (close(from) == -1) {
        perror("cat");
        return errno;
    }
    
    return 0;
}

int do_dir() {
    int ret;
    DIR * dd;
    struct dirent * de;
    char * dir;
    if (arguments[1]) {
        dir = arguments[1];
    } else {
        dir = ".";
    }

    if ((dd = opendir(dir)) == NULL) {
        perror("dir");
        return errno;
    }

    errno = 0;
    while ((de = readdir(dd)) != NULL) {
        printf("%s\n",de->d_name);
    }
    if (errno != 0) {
        perror("dir");
        int ret = errno;
        if (closedir(dd) == -1) {
            perror("dir");
        }
        return ret;
    }

    if (closedir(dd) == -1) {
        perror("dir");
        return errno;
    }

    return 0;
}

int do_ls() {
    int ret;
    DIR * dd;
    struct dirent * de;
    char * dir;
    char file[MAX_PATH_LENGTH];
    char * start;
    struct stat buf;
    char mode[11];
    char datetime[MAX_PATH_LENGTH];
    mode[10] = 0;

    if (arguments[1]) {
        dir = arguments[1];
    } else {
        dir = ".";
    }

    if ((dd = opendir(dir)) == NULL) {
        perror("ls");
        return errno;
    }

    strcpy(file,dir);
    start = file + strlen(file);
    if (*(start-1) != '/') {
        *(start++) = '/';
    }

    while ((de = readdir(dd)) != NULL) {
        strcpy(start,de->d_name);
        if (stat(file,&buf) == -1) {
            //Cygwin tukaj zajebava... bomo vidli kako in kaj
            /*perror("ls");
            printf("%s\n",file);
            int ret = errno;
            if (closedir(dd) == -1) {
                perror("ls");
            }
            return ret;*/
            buf.st_mode = 0;
            buf.st_size = 0;
        }
        errno = 0;
        if (S_ISBLK(buf.st_mode)) {
            mode[0] = 'b';
        } else if (S_ISCHR(buf.st_mode)) {
            mode[0] = 'c';
        } else if (S_ISDIR(buf.st_mode)) {
            mode[0] = 'd';
        } else if (S_ISFIFO(buf.st_mode)) {
            mode[0] = 'f';
        } else if (S_ISLNK(buf.st_mode)) {
            mode[0] = 'l';
        } else if (!S_ISREG(buf.st_mode)) {
            mode[0] = 's';
        } else {
            mode[0] = '-';
        }
        if (buf.st_mode&S_IRUSR) {
            mode[1] = 'r';
        } else {
            mode[1] = '-';
        }
        if (buf.st_mode&S_IWUSR) {
            mode[2] = 'w';
        } else {
            mode[2] = '-';
        }
        if (buf.st_mode&S_IXUSR) {
            mode[3] = 'x';
        } else {
            mode[3] = '-';
        }
        if (buf.st_mode&S_IRGRP) {
            mode[4] = 'r';
        } else {
            mode[4] = '-';
        }
        if (buf.st_mode&S_IWGRP) {
            mode[5] = 'w';
        } else {
            mode[5] = '-';
        }
        if (buf.st_mode&S_IXGRP) {
            mode[6] = 'x';
        } else {
            mode[6] = '-';
        }
        if (buf.st_mode&S_IROTH) {
            mode[7] = 'r';
        } else {
            mode[7] = '-';
        }
        if (buf.st_mode&S_IWOTH) {
            mode[8] = 'w';
        } else {
            mode[8] = '-';
        }
        if (buf.st_mode&S_IXOTH) {
            mode[9] = 'x';
        } else {
            mode[9] = '-';
        }
        //strftime(datetime,MAX_PATH_LENGTH,"%d.%m.%Y %T",localtime(&(buf.st_mtime)));

        printf("%s %-32s %10d\n",mode,de->d_name,buf.st_size);
    }
    if (errno != 0) {
        perror("ls");
        int ret = errno;
        if (closedir(dd) == -1) {
            perror("ls");
        }
        return ret;
    }

    if (closedir(dd) == -1) {
        perror("ls");
        return errno;
    }

    return 0;
}

int do_date() {
    char datetime[MAX_PATH_LENGTH];
    time_t t = time(NULL);
    strftime(datetime,MAX_PATH_LENGTH,"%d.%m.%Y %T",localtime(&t));
    printf("%s\n",datetime);
    return 0;
}

int do_kill() {
    int pid, sig;
    if (argument_count < 3) {
        printf("Uporaba: kill pid signal\n");
        return 1;
    }
    sscanf(arguments[1],"%d",&pid);
    sscanf(arguments[2],"%d",&sig);
    if (kill(pid,sig) == -1) {
        perror("kill");
        return errno;
    }
    return 0;
}

int do_trap() {
    int sig;
    if (argument_count < 3) {
        printf("Uporaba: trap ukaz signal\n");
        return 1;
    }
    sscanf(arguments[2],"%d",&sig);
    if (*arguments[1]) {
        trap[sig] = strdup(arguments[1]);
    } else {
        trap[sig] = NULL;
    }
    return 0;
}

int execute_builtin(int cmdIndex) {
    int status = 0;
    switch (cmdIndex) {
        case CMD_HELP:   status = do_help(); break;
        case CMD_STATUS: status = 0; printf("%i\n", last_command_status); break;
        case CMD_PID:    status = 0; printf("%i\n", shell_pid); break; 
        case CMD_PRINT:  status = do_print(); break;
        case CMD_ECHO:   status = do_echo(); break;
        case CMD_PWD:    status = do_pwd(); break;
        case CMD_CD:     status = do_cd();  break;
        case CMD_MKDIR:  status = do_mkdir(); break;
        case CMD_RMDIR:  status = do_rmdir(); break;
        case CMD_LINK:   status = do_link(); break;
        case CMD_UNLINK: status = do_unlink(); break;
        case CMD_REMOVE: status = do_remove(); break;
        case CMD_RENAME: status = do_rename(); break;
        case CMD_COPY:   status = do_copy(); break;
        case CMD_CAT:    status = do_cat(); break;
        case CMD_DIR:    status = do_dir(); break;
        case CMD_LS:     status = do_ls(); break;
        case CMD_DATE:   status = do_date(); break;
        case CMD_KILL:   status = do_kill(); break;
        case CMD_TRAP:   status = do_trap(); break;
        case CMD_EXIT:
        case CMD_QUIT:
        case CMD_BYE:    printf("Na svidenje.\n"); exit(0); break;
        case CMD_DEBUG:  status = 0; debug = !debug; break;
        default:
            printf("Nepravilen indeks ukaza.\n");
            abort();
    }
    return status;
}

int execute_command() {
    int cmdIndex = is_builtin(arguments[0]);
    if (cmdIndex >= 0)
        last_command_status = execute_builtin(cmdIndex);
    else {
        last_command_status = 1;
        printf("Neznan ukaz.\n");
    }
    return last_command_status;
}

void signal_handler(int sig) {
    int status;
    if (trap[sig]) {
        //sscanf(trap[sig],"%s",command_line);
        parse_command_line(trap[sig]);
        execute_command();
    } else {
        switch (sig) {
            case SIGCHLD: wait(&status); break;
            case SIGTTOU: tcsetpgrp(shell_terminal, shell_pgid); break;
        }
    }
    if (debug) fprintf(stderr, "%i: Ujel signal %i.\n", getpid(), sig);
}

void init_shell() {
    trap = calloc(64,sizeof(char*));
    shell_terminal = STDIN_FILENO;
    // Postavi se v foreground
    while (tcgetpgrp (shell_terminal) != (shell_pgid = getpgrp ()))
        kill (-shell_pgid, SIGTTIN);
    // PID in PGID procesa
    shell_pid = getpid();
    shell_pgid = getpgid(shell_pid);
    // Lovljenje signalov
    signal (SIGINT, signal_handler);
    signal (SIGQUIT, signal_handler);
    signal (SIGTSTP, signal_handler);
    signal (SIGTTIN, signal_handler);
    signal (SIGTTOU, signal_handler);
    signal (SIGCHLD, signal_handler);
     // Ustvarimo svojo PGID
    if (setpgid(shell_pid, shell_pgid) == -1) {
        perror("init_shell: setpgid");
        exit(1);
    }
    // Se povezemo s terminalom
    if (tcsetpgrp(shell_terminal, shell_pgid) == -1)
        perror("init_shell: tcsetpgrp");
}

int main(int argc, char ** argv) {
    printf("                     ___              \n");
    printf("                    /  \\\\             \n");
    printf("             ______ \\  //             \n");
    printf("            /      \\/ o \\             \n");
    printf("   ________/             \\            \n");
    printf("           \\_  \\__\\ \\-----o           \n");
    printf(" VK          \\____|\\__|               \n\n");
    printf("mysh 0.1\n");
    printf("Avtor: Janos Vidali\n");
    printf("po predlogi: http://lalg.fri.uni-lj.si/~jure/oos/mysh_template.c\n");
    printf("ASCII art: http://www.ludd.luth.se/~vk/pics/ascii/ASCII1.HTML#comments\n\n");
    
    init_shell();

    while (1) {
        // Preberi ukazno vrstico
        print_prompt();
        read_command_line();
        if (argument_count == 0) continue;
        // Poisci index vgrajenega ukaza
        execute_command();
    }
}
