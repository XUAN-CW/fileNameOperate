import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用途：本类用于文件重命名，创建本类时指定文件，
 * 警告：谨慎使用！程序一旦运行，便不可撤回！注意路径的填写！
 * 遇到的问题：转义问题 <=> 解决方法：在字符前加 【\\】
 * 来源：https://www.cnblogs.com/CodeKjm/p/9615815.html
 */

public class Rename {
	interface RenameRule {
		String rename(String oldName);
	}

	private File file;// 要重命名的文件

	public Rename(File file) throws Exception {
		setFile(file);
	}

	public RenameRule delete(String Deleted) {
		RenameRule renameRule = (String oldName) -> {
			return oldName.replaceAll(Deleted, "");// 新名字
		};
		return renameRule;
	}

	public RenameRule replace(String regex, String replacement) {
		RenameRule renameRule = (String oldName) -> {
			return oldName.replaceAll(regex, replacement);// 新名字
		};
		return renameRule;
	}

	/**
	 * 加前缀
	 */
	public RenameRule addPrefix(String prefix) {
		RenameRule renameRule = (String oldName) -> {
			return prefix + oldName;// 新名字
		};
		return renameRule;
	}

	/**
	 * 加后缀
	 */
	public RenameRule addPostfix(String postfix) {
		RenameRule renameRule = (String oldName) -> {
			return oldName + postfix;// 新名字
		};
		return renameRule;
	}

	/**
	 * 删除文件名的前 x 个字符，若文件名长度小于 x ，则保留原文件名
	 */
	public RenameRule DeleteCharacters(int x) {
		RenameRule renameRule = (String oldName) -> {
			return oldName.length() > x ? oldName.substring(x) : oldName;// 新名字
		};
		return renameRule;
	}

	/**
	 * 将文件名中的某字符串提到前面
	 */
	public RenameRule bringTheStringForward(String str) {
		RenameRule renameRule = (String oldName) -> {
			return str + oldName.replaceFirst(str, "");
		};
		return renameRule;
	}

	/**
	 * 以数字为名的文件重命名后该数字加上 x 例如 【1.txt】,x 为 2 ，则重命名后 【3.txt】
	 */
	public RenameRule addX(int x) {
		RenameRule renameRule = (String oldName) -> {
			String newName = oldName;
			Pattern p = Pattern.compile("\\d+");
			// 创建Matcher对象
			Matcher m = p.matcher(oldName);
			if (m.find()) {// 找到了数字

//				重命名的时候可能遇到这么一种情况：
//				1.txt】重命名为 【3.txt】，原先又存在  【3.txt】，
//			        这种情况下重命名会失败，所以我给新文件名加上 ABCDEFGHIJK （应该没有哪个文件包含有这个字符串吧）
//				之后删除 ABCDEFGHIJK 即可
				newName = "ABCDEFGHIJK"
						+ oldName.replaceFirst(m.group(0), String.format("%03d", Integer.valueOf(m.group(0)) + x));
			}
			return newName;
		};
		return renameRule;
	}

	/**
	 * 去除两个字符及里面的内容
	 * 
	 * @return
	 */
	public RenameRule removeCharAndContentInIt(char start, char end) {
		RenameRule renameRule = (String oldName) -> {
			String newName = oldName;
			for (int i = 0; i < 50; i++) {
				if (newName.contains(String.valueOf(start)) && newName.contains(String.valueOf(end))) {
//					System.out.println("前："+newName);
					//截取前一段和后一段，再拼接起来
					newName = newName.substring(0, newName.indexOf(start))
							+ newName.substring(newName.indexOf(end) + 1, newName.length());
//				System.out.println("后："+newName);

				} else {
					break;
				}
			}
//			System.out.println("终："+newName);
			return newName;
		};
		return renameRule;
	}

	/**
	 * 开始重命名
	 * 
	 * @param renameRule
	 * @param isRename
	 */
	public void startRename(RenameRule renameRule, boolean isRename) {
		if (file.exists()) {
			File newFile = new File(file.getParentFile() + "/" + renameRule.rename(file.getName()));
			if (isRename) {// 这里为预览功能而设计
				file.renameTo(newFile);// 重命名
			} else {
				System.out.println("----- 预览 -----");
			}
			System.out.print(newFile.getName().equals(file.getName()) ? "" : "重命名后：" + newFile.getName() + "\n");
		}
	}

	/**
	 * 这里进行文件检测，如果确保文件可用
	 * @param file 传入的参数不为空，且确保文件可用
	 */
	public void setFile(File  file) throws Exception {
		if (null==file){
			throw new NullPointerException();
		}
		if (!file.exists()){
			throw new Exception("文件不存在");
		}
		this.file = file;
	}

}
