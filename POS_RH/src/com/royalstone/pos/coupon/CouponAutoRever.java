package com.royalstone.pos.coupon;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.royalstone.pos.shell.pos;

/**
 * 挂账卡自动冲正,它以后台线程的形式运行，每隔几秒钟就检查autorever子目录下有没有冲正文件，
 * 如果有就透过LoanCard向服务器发出冲正请求
 * @author liangxinbiao
 */
public class CouponAutoRever implements Runnable {

	CouponMgr couponMgr = new CouponMgrImpl();

	/**
	 * @see Runnable#run()
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(10000);
				autoRever();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 冲正
	 */
    private void autoRever() {
        synchronized (pos.Lock) {
            File path = new File("couponrever");
            File[] files = path.listFiles(new DirFilter(".xml"));
            if(files!=null){
                for (int i = 0; i < files.length; i++) {
                    try {
                        Document doc = (new SAXBuilder()).build(files[i]);
                        CouponSaleList couponSales =
                            new CouponSaleList();
                          couponSales.fromElement(doc.getRootElement());
                        String result = couponMgr.autoRever(couponSales);
                        if (result != null && result.equals("1")) {
                            files[i].delete();
                        }
                    } catch (JDOMException e) {
                        e.printStackTrace();
                    }catch(IOException ex){
                    	ex.printStackTrace();
                    } catch (CouponException e) {
						e.printStackTrace();
					}
                }
            }
        }
    }

	/**
	 * 目录过滤器
	 * @author liangxinbiao
	 */
	private class DirFilter implements FilenameFilter {
		String afn;
		DirFilter(String afn) {
			this.afn = afn;
		}
		public boolean accept(File dir, String name) {
			return name.indexOf(afn) != -1;
		}
	}
}
