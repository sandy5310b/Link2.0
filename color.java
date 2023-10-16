// public class color {
//     public static void main(String[] args) {
//         String resetColor = "\u001B[0m";  // Reset text style to default
        
//         // Bold foreground (text) colors
//         String[] boldForegroundColors = {
//             "\u001B[1;30m", // Bold Black
//             "\u001B[1;31m", // Bold Red
//             "\u001B[1;32m", // Bold Green
//             "\u001B[1;33m", // Bold Yellow
//             "\u001B[1;34m", // Bold Blue
//             "\u001B[1;35m", // Bold Magenta
//             "\u001B[1;36m", // Bold Cyan
//             "\u001B[1;37m", // Bold White
//             "\u001B[1;90m", // Bold Gray
//             "\u001B[1;91m", // Bold Light Red
//             "\u001B[1;92m", // Bold Light Green
//             "\u001B[1;93m", // Bold Light Yellow
//             "\u001B[1;94m", // Bold Light Blue
//             "\u001B[1;95m", // Bold Light Magenta
//             "\u001B[1;96m", // Bold Light Cyan
//             "\u001B[1;97m"  // Bold Light White
//         };
        
//         String text = "This is bold ";

//         for (String boldFgColor : boldForegroundColors) {
//             // Combine bold foreground color with text
//             String coloredText = boldFgColor + text + boldFgColor + "color" + resetColor;
//             System.out.println(coloredText);
//         }
//     }
// }
public class color {
    static String bold_blue = "\u001B[1;94m";
    static String boldGreenColor = "\u001B[1;32m";
    static String boldCyanColor = "\u001B[1;36m"; 
    static String resetColor = "\u001B[0m";
    static String bold_yellow = "\u001B[1;33m";
    static String bold_red = "\u001B[1;91m" ;
    public static void main(String[] args) {
        String resetColor = "\u001B[0m";  // Reset text style to default
        
        // Bright and bold foreground (text) colors
        String[] brightBoldForegroundColors = {
            "\u001B[1;90m", // Bold Bright Black
            "\u001B[1;91m", // Bold Bright Red
            "\u001B[1;92m", // Bold Bright Green
            "\u001B[1;93m", // Bold Bright Yellow
            "\u001B[1;94m", // Bold Bright Blue
            "\u001B[1;95m", // Bold Bright Magenta
            "\u001B[1;96m", // Bold Bright Cyan
            "\u001B[1;97m"  // Bold Bright White
        };
        
        String text = "This is bold bright ";
        
        for (String brightBoldFgColor : brightBoldForegroundColors) {
            // Combine bold and bright foreground color with text
            String coloredText = brightBoldFgColor + text + brightBoldFgColor + "color" + resetColor;
            System.out.println(coloredText);
        }
    }
}
