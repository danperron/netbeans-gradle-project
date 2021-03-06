package org.netbeans.gradle.project.java.tasks;

import javax.swing.text.Document;
import org.jtrim.utils.ExceptionHelper;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.gradle.project.NbGradleProject;
import org.netbeans.gradle.project.api.task.GradleActionProviderContext;
import org.netbeans.gradle.project.properties.global.GlobalGradleSettings;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.spi.editor.document.OnSaveTask;
import org.netbeans.spi.project.ActionProvider;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.Lookups;

public final class CompileOnSaveAction implements OnSaveTask {
    private final FileObject srcFile;

    public CompileOnSaveAction(FileObject srcFile) {
        ExceptionHelper.checkNotNullArgument(srcFile, "srcFile");

        this.srcFile = srcFile;
    }

    @Override
    public void performTask() {
        if (!GlobalGradleSettings.getDefault().compileOnSave().getValue()) {
            return;
        }

        Project project = FileOwnerQuery.getOwner(srcFile);
        if (project == null || !isGradleProject(project)) {
            return;
        }

        ActionProvider actionProvider
                = project.getLookup().lookup(ActionProvider.class);
        if (actionProvider == null) {
            return;
        }
        actionProvider.invokeAction(
                JavaProjectConstants.COMMAND_DEBUG_FIX,
                Lookups.fixed(srcFile,
                        GradleActionProviderContext.DONT_SAVE_FILES,
                        GradleActionProviderContext.DONT_FOCUS_ON_OUTPUT));
    }

    private static boolean isGradleProject(Project project) {
        return project.getLookup().lookup(NbGradleProject.class) != null;
    }

    @Override
    public void runLocked(Runnable run) {
        run.run();
    }

    @Override
    public boolean cancel() {
        return false;
    }

    @MimeRegistration(mimeType = "", service = OnSaveTask.Factory.class, position = 30000)
    public static final class CompileOnSaveActionFactory implements Factory {
        @Override
        public OnSaveTask createTask(Context context) {
            Document document = context.getDocument();
            if (document == null) {
                return null;
            }

            Source source = Source.create(document);
            FileObject srcFile = source.getFileObject();
            return srcFile != null ? new CompileOnSaveAction(srcFile) : null;
        }
    }
}
