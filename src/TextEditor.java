import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对某个文本文件进行编辑
 * 调用 startEdit() 开始编辑
 */

public class TextEditor {
	// 这个接口是为lambda表达式准备的，就不写在外面了
	interface Editor {
		String edit(String content);
	}

	File file;

	public TextEditor(File editedFile) {
		setFile(editedFile);
	}


	private String read() {
		String content = "";
		// 增加缓存 - 读
		FileInputStream fInputStream = null;
		BufferedInputStream buffInputStream = null; // 防止无法关闭，放在try/catch外面
		try {
			fInputStream = new FileInputStream(file);
			buffInputStream = new BufferedInputStream(fInputStream, 1024 * 10); // 不要太大，迅雷也就几十M
			byte[] bytes = new byte[buffInputStream.available()];
			buffInputStream.read(bytes);
			content = new String(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // 先关缓存流，再关文件流
			try {
				buffInputStream.close();
				fInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	private void save(String content) {
		// 清空该文件
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("");
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 增加缓存 - 写
		FileOutputStream fOutputStream = null;
		BufferedOutputStream buffOutputStream = null; // 防止无法关闭，放在try/catch外面
		try {
			fOutputStream = new FileOutputStream(file);
			buffOutputStream = new BufferedOutputStream(fOutputStream, 1024 * 10);// 不要太大，迅雷也就几十M
			buffOutputStream.write(content.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				buffOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void startEdit(Editor editor) {
		if (file.exists()) {
			// 读取后编辑，最后保存回原文件
			save(editor.edit(read()));
		}
	}

	private void setFile(File file) {
		this.file = file.exists() ? file : null;
	}


}
