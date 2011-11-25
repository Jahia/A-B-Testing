package org.jahia.modules.abtesting;

import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;
import org.jahia.services.render.filter.RenderFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: toto
 * Date: 11/9/11
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ABTestingFilter extends AbstractFilter {
    private Random random = new Random();
    private volatile int current = 0;

    @Override
    public String prepare(RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        if (resource.getContextConfiguration().equals("page")) {
            Integer i = (Integer) renderContext.getRequest().getSession().getAttribute("abtest-"+resource.getNode().getIdentifier());
            if (i == null) {
            current ++;
            current %= (int) resource.getNode().getProperty("j:numberOfVersions").getLong();
                i = current; //random.nextInt((int) resource.getNode().getProperty("j:numberOfVersions").getLong());
                renderContext.getRequest().getSession().setAttribute("abtest-"+resource.getNode().getIdentifier(), i);
            }
            renderContext.getRequest().setAttribute("abtesting",i);
            renderContext.getRequest().setAttribute("analytics-path", resource.getNode().getUrl()+".abtest-"+i );
        }

        List l = (List) renderContext.getRequest().getAttribute("module.cache.additional.key");
        if (l == null) {
            l = new ArrayList();
            renderContext.getRequest().setAttribute("module.cache.additional.key",l);
        }

        l.add("abtest" + renderContext.getRequest().getAttribute("abtesting"));

        return super.prepare(renderContext, resource, chain);
    }
}
