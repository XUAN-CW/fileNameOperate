import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 经验：
 * * 对文件和文件夹命名不能使用以下9个字符：
 *   / \ : * " < >  ？
 *   否则会导致 File.mkdir() 失败
 */

public class Main {
	public static void main(String[] args) throws Exception {
		GetFile getFile = new GetFile(new File("C:\\Users\\18888484810\\Desktop\\eNSP"));
		for (File file:getFile.getFolders(getFile.selectNameContains("任务"))) {
			
		}
	}
}
