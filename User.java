//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class User {
    private static final File userFile = new File("users.txt");

    public User() {
    }

    public static boolean register(String username, String password) {
        if (userExists(username)) {
            return false;
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true));

                boolean var3;
                try {
                    writer.write(username + ":" + password);
                    writer.newLine();
                    var3 = true;
                } catch (Throwable var6) {
                    try {
                        writer.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }

                    throw var6;
                }

                writer.close();
                return var3;
            } catch (IOException var7) {
                IOException e = var7;
                e.printStackTrace();
                return false;
            }
        }
    }

    public static boolean login(String username, String password) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFile));

            boolean var5;
            label57: {
                String line;
                try {
                    while((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                            var5 = true;
                            break label57;
                        }
                    }
                } catch (Throwable var7) {
                    try {
                        reader.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }

                    throw var7;
                }

                reader.close();
                return false;
            }

            reader.close();
            return var5;
        } catch (IOException var8) {
            IOException e = var8;
            e.printStackTrace();
            return false;
        }
    }

    private static boolean userExists(String username) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFile));

            boolean var4;
            label53: {
                String line;
                try {
                    while((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        if (parts.length == 2 && parts[0].equals(username)) {
                            var4 = true;
                            break label53;
                        }
                    }
                } catch (Throwable var6) {
                    try {
                        reader.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }

                    throw var6;
                }

                reader.close();
                return false;
            }

            reader.close();
            return var4;
        } catch (IOException var7) {
            IOException e = var7;
            e.printStackTrace();
            return false;
        }
    }

    static {
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
            } catch (IOException var1) {
                IOException e = var1;
                e.printStackTrace();
            }
        }

    }
}
