import javafx.util.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private JPanel frmMain;
    private JTabbedPane tabbedPane1;
    private JTextArea txtRaw;
    private JButton btnCompress;
    private JButton btnDecompress;
    private JList<String> lstDictionary;
    private JTextArea txtCompressed;
    private JTextField txtBrowseRaw;
    private JButton btnBrowseRaw;
    private JTextField txtBrowseCompressed;
    private JButton btnBrowseCompressed;
    private JButton btnCompressRaw;
    private JButton btnDecompressFile;

    private DefaultListModel<String> dlm;
    private JFileChooser fileChooser;

    public Main() {
        fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);

        dlm = new DefaultListModel<>();
        lstDictionary.setModel(dlm);
        btnCompress.addActionListener(this::btnCompress_Clicked);
        btnDecompress.addActionListener(this::btnDecompress_Clicked);

        btnBrowseCompressed.addActionListener(this::btnBrowseCompressed_Clicked);
        btnBrowseRaw.addActionListener(this::btnBrowseRaw_Clicked);
        btnDecompressFile.addActionListener(this::btnDecompressFile_Clicked);
        btnCompressRaw.addActionListener(this::btnCompressRaw_Clicked);
    }

    private void btnDecompressFile_Clicked(ActionEvent actionEvent) {
        try {
            byte[] res = LZ78.decompressFromArray(Files.readAllBytes(Paths.get(txtBrowseCompressed.getText())));
            int retvalue = fileChooser.showSaveDialog(null);
            if (retvalue != JFileChooser.APPROVE_OPTION) return;
            Files.write(fileChooser.getSelectedFile().toPath(), res);
            JOptionPane.showMessageDialog(null, "File saved!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    private void btnCompressRaw_Clicked(ActionEvent actionEvent) {
        try {

            byte[] res = LZ78.compressToArray(Files.readAllBytes(Paths.get(txtBrowseRaw.getText())));

            int retvalue = fileChooser.showSaveDialog(null);
            if (retvalue != JFileChooser.APPROVE_OPTION) return;
            Files.write(fileChooser.getSelectedFile().toPath(), res);
            JOptionPane.showMessageDialog(null, "File saved!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void btnBrowseRaw_Clicked(ActionEvent actionEvent) {
        int retvalue = fileChooser.showOpenDialog(null);
        if (retvalue == JFileChooser.APPROVE_OPTION) {
            txtBrowseRaw.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void btnBrowseCompressed_Clicked(ActionEvent actionEvent) {
        int retvalue = fileChooser.showOpenDialog(null);
        if (retvalue == JFileChooser.APPROVE_OPTION) {
            txtBrowseCompressed.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void btnCompress_Clicked(ActionEvent e) {
        String msg = txtRaw.getText();
        List<Pair<Integer, Character>> ret = LZ78.compress(msg);
        dlm.clear();
        for (int i = 0; i < LZ78.getLastDictionary().size(); ++i)
            dlm.addElement(i + " : " + LZ78.getLastDictionary().get(i));

        txtCompressed.setText("");
        for (Pair<Integer, Character> tag : ret) {
            txtCompressed.append(String.format("<%d,%c>", tag.getKey(), tag.getValue()));
        }
    }

    private void btnDecompress_Clicked(ActionEvent e) {
        String compressed = txtCompressed.getText();
        List<Pair<Integer, Character>> tags = new ArrayList<>();
        Pattern regex = Pattern.compile("<([0-9]+),([^<>]+)>");
        Matcher regexMatcher = regex.matcher(compressed);
        while (regexMatcher.find()) {
            String match2 = regexMatcher.group(2);
            Character c = null;
            if (!match2.equals("null")) c = match2.charAt(0);
            tags.add(new Pair<>(Integer.parseInt(regexMatcher.group(1)), c));
        }
        String msg;
        try {
            msg = LZ78.decompress(tags);
            dlm.clear();
            for (int i = 0; i < LZ78.getLastDictionary().size(); ++i)
                dlm.addElement(i + " : " + LZ78.getLastDictionary().get(i));

        } catch (Exception ex) {
            msg = ex.getMessage();
        }
        txtRaw.setText(msg);

    }

    public static void main(String[] args) {
        if (args != null && args.length != 0 && args[0].equals("--cmd")) {
            Console.showConsoleMenu();
            return;
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        JFrame frame = new JFrame("LZ78 Demonstration");
        frame.setContentPane(new Main().frmMain);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
