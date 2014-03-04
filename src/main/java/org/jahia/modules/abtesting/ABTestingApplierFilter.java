package org.jahia.modules.abtesting;

import org.jahia.services.content.JCRContentUtils;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.nodetypes.NodeTypeRegistry;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.Template;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;

import java.util.List;

public class ABTestingApplierFilter extends AbstractFilter {

    @Override
    public String prepare(RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        int i = 0;
        if (renderContext.getRequest().getAttribute("abtesting") != null && renderContext.getRequest().getAttribute("abtestingdone") == null) {
            i = (Integer) renderContext.getRequest().getAttribute("abtesting");

            renderContext.getRequest().setAttribute("abtestingdone", Boolean.TRUE);
        } else if (renderContext.isEditMode() || renderContext.isContributionMode()) {
            if (renderContext.getRequest().getParameter("alt") != null) {
                i = Integer.parseInt(renderContext.getRequest().getParameter("alt"));
            }
        }
        if (i > 0) {
            Template template = service.resolveTemplate(resource, renderContext);
            renderContext.getRequest().setAttribute("templateSet", Boolean.TRUE);
            renderContext.getRequest().setAttribute("cachedTemplate", template);

            JCRNodeWrapper node = resource.getNode();
            if (!node.hasNode("alt"+i) && (renderContext.isEditMode() || renderContext.isContributionMode())) {
                JCRNodeWrapper alt = node.addNode("alt"+i, "jnt:abTestAlternative");
                List<JCRNodeWrapper> n = JCRContentUtils.getChildrenOfType(node, "jnt:contentList");
                for (JCRNodeWrapper area : n) {
                    if (!area.isNodeType("jnt:abTestAlternative")) {
                        area.copy(alt.getPath());
                    }
                }
                alt.getSession().save();
            }
            if (node.hasNode("alt"+i)) {
                while (template.next != null) {
                    template = template.next;
                }
                template.next = new Template(null,node.getNode("alt"+i).getIdentifier(),null, "abtesting"+i);
                resource.getModuleParams().put("templateEdit",template.next.node);
            }
        }
        return super.prepare(renderContext, resource, chain);
    }

    @Override
    public String execute(String previousOut, RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        StringBuffer out = new StringBuffer(previousOut);
        if ((renderContext.isContributionMode() || renderContext.isEditMode()) && !renderContext.isAjaxRequest()) {
            Resource resource1 = new Resource(resource.getNode(), "html",
                    "hidden.toolbar", Resource.CONFIGURATION_INCLUDE);
            resource1.setResourceNodeType(NodeTypeRegistry.getInstance().getNodeType("jmix:abtesting"));
            final String toolbarBegin = service.render(resource1, renderContext);
            out.insert(out.indexOf(">", out.indexOf("<body"))+1,toolbarBegin);
        }

        return out.toString();
    }
}
