package org.netbeans.gradle.project;

import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import org.jtrim.property.PropertyFactory;
import org.jtrim.property.PropertySource;
import org.jtrim.swing.concurrent.SwingTaskExecutor;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.gradle.project.properties.SwingPropertyChangeForwarder;
import org.openide.util.ImageUtilities;

public final class GradleProjectInformation implements ProjectInformation {
    private final NbGradleProject project;
    private final SwingPropertyChangeForwarder changeListeners;

    public GradleProjectInformation(NbGradleProject project) {
        this.project = project;

        SwingPropertyChangeForwarder.Builder combinedListeners
                = new SwingPropertyChangeForwarder.Builder(SwingTaskExecutor.getStrictExecutor(false));

        PropertySource<String> displayName = PropertyFactory.lazilyNotifiedSource(project.displayName());
        combinedListeners.addProperty(PROP_DISPLAY_NAME, displayName);
        this.changeListeners = combinedListeners.create();
    }

    @Override
    public Icon getIcon() {
        return ImageUtilities.image2Icon(NbIcons.getGradleIcon());
    }

    @Override
    public String getName() {
        return project.getName();
    }

    @Override
    public String getDisplayName() {
        return project.displayName().getValue();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        changeListeners.addPropertyChangeListener(pcl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        changeListeners.removePropertyChangeListener(pcl);
    }

    @Override
    public Project getProject() {
        return project;
    }

}
