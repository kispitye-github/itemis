package hu.kispitye.itemis;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

public class GitHubHack extends TransactionalTestExecutionListener {
	public GitHubHack()  {}

	@Override
	public void beforeTestMethod(final TestContext testContext) throws Exception {
		try  {
			super.beforeTestMethod(testContext);
		} catch (Exception e)  {
System.err.println("!!!!HACK!!!!"+e); //TODO
			afterTestMethod(testContext);
			super.beforeTestMethod(testContext);
		}
	}
	
}
