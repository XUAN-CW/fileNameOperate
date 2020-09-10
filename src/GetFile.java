import java.io.File;
import java.util.Stack;

/**
 * 创建此类后调用 getFolders 可获取指定目录下所有文件夹，调用 getFiles 可获取指定目录下所有文件夹
 */
public class GetFile {
	private File root;// 传入的目录为根目录
	/**
	 * 为什么要用栈存储？ 
	 * 为了防止文件夹因重命名而找不到路径
	 */
	private Stack<File> folder = new Stack<File>();// 根目录下的所有文件夹
	private Stack<File> files = new Stack<File>(); // 根目录下的所有文件

	/**
	 * 内部接口，判断文件是否选中
	 */
	interface SelectMode {
		boolean startSelect(File file);
	}

	public GetFile(File dir) throws Exception {
		setRoot(dir);// 在一开始就设置好根目录
	}

	public void setRoot(File root) throws Exception {
		if (null==root){
			throw new NullPointerException();
		}
		if (!root.exists()){
			throw new Exception("文件不存在");
		}
		this.root = root;
	}

	/**
	 * 获取根目录下的所有文件夹
	 * @return
	 */
	public Stack<File> getFolders(SelectMode selectMode) {
		while (!folder.empty()) {
			folder.pop();
		}
		recursiveTraversalFolder(selectMode,root);// 递归遍历根目录下的所有文件，获取文件夹
		return folder;
	}

	/**
	 * 获取根目录下的所有文件（不包含文件夹）
	 * @return
	 */
	public Stack<File> getFiles(SelectMode selectMode) {
		while (!files.empty()) {
			files.pop();
		}
		recursiveTraversalFolder(selectMode,root);// 递归遍历根目录下的所有文件，获取 文件
		return files;
	}

	private void recursiveTraversalFolder(SelectMode selectMode, File currentDir) {
		// 获取当前文件夹下所有 文件/文件夹
		File[] fileArr = currentDir.listFiles();
		if (null == fileArr || fileArr.length == 0) {
//				System.out.println("["+temp.getAbsolutePath()+"]"+" 为空!");
			return;
		} else {
			// 在操作当前目录下所有 文件/文件夹
			for (File file : fileArr) {
				if (file.isDirectory()) {
					if (selectMode.startSelect(file)) {
						folder.push(file);// 符合条件的文件夹入栈
					}
					recursiveTraversalFolder(selectMode, file);// 是文件夹，继续往下递归
				} else {
					if (selectMode.startSelect(file)) {
						files.push(file);// 符合条件的文件入栈
					}
				}
			}
		}
	}

//⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇ 返回 已实现的 SelectMode ，便于 getFiles、getFolders 调用 ⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇

	/**
	 * 根据后缀选择
	 * @param postfixes
	 * @return
	 */
	public SelectMode selectByPostfix(String... postfixes) {
		return (File file) ->{
			for (String postfix : postfixes) {
				//文件名还没有输入的后缀长，直接返回 false
				//文件名比输入的后缀长，截取文件名最右边与后缀长度相同的子串，进行比较
				if (file.getName().length() > postfix.length() &&
						file.getName().substring(file.getName().length() - postfix.length()).equals(postfix)) {
					return true;
				}
			}
			return false;
		};
	}

	/**
	 * 选择文件名为 name 的文件
	 * @param name
	 * @return
	 */
	public SelectMode selectNameEquals(String name) {
		return (File f) -> f.getName().equals(name);
	}

	/**
	 * 选择包含 str 的文件
	 * @param str
	 * @return
	 */
	public SelectMode selectNameContains(String str) {
		return (File f) -> f.getName().contains(str);
	}
}
