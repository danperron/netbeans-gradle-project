package org.netbeans.gradle.project.java.properties;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import org.jtrim.utils.ExceptionHelper;
import org.netbeans.api.project.Project;
import org.netbeans.gradle.project.api.config.ActiveSettingsQuery;
import org.netbeans.gradle.project.api.config.ProjectSettingsProvider;
import org.netbeans.gradle.project.api.config.PropertyReference;
import org.netbeans.gradle.project.java.JavaExtension;
import org.netbeans.gradle.project.properties.DebugModeCombo;
import org.netbeans.gradle.project.properties.ProfileBasedCustomizer;
import org.netbeans.gradle.project.properties.ProfileBasedPanel;
import org.netbeans.gradle.project.properties.ProfileValuesEditor;
import org.netbeans.gradle.project.properties.ProfileValuesEditorFactory;
import org.netbeans.gradle.project.properties.global.DebugMode;
import org.netbeans.gradle.project.util.NbGuiUtils;

@SuppressWarnings("serial")
public class JavaDebuggingPanel extends javax.swing.JPanel {
    private final DebugModeCombo debugModeComboHandler;

    public JavaDebuggingPanel() {
        initComponents();
        debugModeComboHandler = new DebugModeCombo(jDebugMode);

        setupEnableDisable();
    }

    private void setupEnableDisable() {
        setupInheritCheck(jDebugModeInherit, jDebugMode);
    }

    private static void setupInheritCheck(JCheckBox inheritCheck, JComponent... components) {
        NbGuiUtils.enableBasedOnCheck(inheritCheck, false, components);
    }

    private static <Value> Value setInheritAndGetValue(
            Value value,
            PropertyReference<? extends Value> valueWithFallbacks,
            JCheckBox inheritCheck) {
        inheritCheck.setSelected(value == null);
        return value != null ? value : valueWithFallbacks.getActiveValue();
    }

    public static ProfileBasedCustomizer createDebuggingCustomizer(final JavaExtension javaExt) {
        ExceptionHelper.checkNotNullArgument(javaExt, "javaExt");
        ProfileBasedCustomizer.PanelFactory panelFactory = new ProfileBasedCustomizer.PanelFactory() {
            @Override
            public ProfileBasedPanel createPanel() {
                return createProfileBasedPanel(javaExt);
            }
        };

        return new ProfileBasedCustomizer(
                JavaDebuggingPanel.class.getName(),
                // TODO: I18N
                "Debugging - Java",
                panelFactory);
    }

    public static ProfileBasedPanel createProfileBasedPanel(JavaExtension javaExt) {
        Project project = javaExt.getProject();
        ProjectSettingsProvider.ExtensionSettings extensionSettings = javaExt.getExtensionSettings();
        final JavaDebuggingPanel customPanel = new JavaDebuggingPanel();
        return ProfileBasedPanel.createPanel(project, extensionSettings, customPanel, new ProfileValuesEditorFactory() {
            @Override
            public ProfileValuesEditor startEditingProfile(String displayName, ActiveSettingsQuery profileQuery) {
                return customPanel.new PropertyValues(profileQuery);
            }
        });
    }

    private final class PropertyValues implements ProfileValuesEditor {
        public final PropertyReference<DebugMode> debugModeRef;
        private DebugMode currentDebugMode;

        public PropertyValues(ActiveSettingsQuery settings) {
            this.debugModeRef = JavaProjectProperties.debugMode(settings);
            this.currentDebugMode = debugModeRef.tryGetValueWithoutFallback();
        }

        @Override
        public void displayValues() {
            DebugMode activeDebugMode = setInheritAndGetValue(currentDebugMode, debugModeRef, jDebugModeInherit);
            debugModeComboHandler.setSelectedDebugMode(activeDebugMode);
        }

        @Override
        public void readFromGui() {
            currentDebugMode = jDebugModeInherit.isSelected() ? null : debugModeComboHandler.getSelectedDebugMode();
        }

        @Override
        public void applyValues() {
            debugModeRef.setValue(currentDebugMode);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDebugMode = new javax.swing.JComboBox<>();
        jDebugModeCaption = new javax.swing.JLabel();
        jDebugModeInherit = new javax.swing.JCheckBox();

        org.openide.awt.Mnemonics.setLocalizedText(jDebugModeCaption, org.openide.util.NbBundle.getMessage(JavaDebuggingPanel.class, "JavaDebuggingPanel.jDebugModeCaption.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jDebugModeInherit, org.openide.util.NbBundle.getMessage(JavaDebuggingPanel.class, "JavaDebuggingPanel.jDebugModeInherit.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDebugModeCaption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDebugMode, 0, 185, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jDebugModeInherit)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDebugModeCaption)
                    .addComponent(jDebugMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDebugModeInherit))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<DebugModeCombo.Item> jDebugMode;
    private javax.swing.JLabel jDebugModeCaption;
    private javax.swing.JCheckBox jDebugModeInherit;
    // End of variables declaration//GEN-END:variables
}
