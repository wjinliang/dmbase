import com.dm.cms.model.CmsSite;
import com.dm.cms.service.CmsSiteService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by cgj on 2015/11/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/config/spring/applicationContext.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class CmsSiteTest {
    public static Logger logger = Logger.getLogger(CmsSiteTest.class);
    @Autowired CmsSiteService cmsSiteService;

    @Rollback(true)
    @Test public void insert(){
        CmsSite cmsSite = new CmsSite();
        cmsSite.setDisplayName("test");
        cmsSite.setDomain("test");
        cmsSite.setIsHtml(false);
        cmsSite.setTemplateId(1);
        cmsSiteService.insertCmsSite(cmsSite);
        list();
    }
    @Test public void list(){
        PageInfo<CmsSite> page = cmsSiteService.findCmsSite(0,15,null);
        System.out.println(page.getTotal()+"==================================");
    }

}
