package com.netflix.gradle.plugins.packaging

import com.netflix.gradle.plugins.deb.control.MultiArch
import org.redline_rpm.header.Architecture
import org.redline_rpm.header.Os
import org.redline_rpm.header.RpmType
import org.redline_rpm.payload.Directive
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Extension that can be used to configure both DEB and RPM.
 *
 * Ideally we'd have multiple levels, e.g. an extension for everything, one for deb which extends the, one for rpm
 * that extends the base and have all the tasks take from the specific extension. But since tasks.withType can be used,
 * there isn't really a need for "all rpm" extension.
 */

class SystemPackagingExtension {
    // File name components
    @Input @Optional
    String packageName

    @Input @Optional
    String release

    @Input @Optional
    String version

    @Input @Optional
    Integer epoch

    // Metadata, some are probably specific to a type
    @Input @Optional
    String user

    @Input @Optional
    String permissionGroup // Group is used by Gradle on tasks.

    /**
     * In Debian, this is the Section and has to be provided. Valid values are: admin, cli-mono, comm, database, debug,
     * devel, doc, editors, education, electronics, embedded, fonts, games, gnome, gnu-r, gnustep, graphics, hamradio,
     * haskell, httpd, interpreters, introspection, java, kde, kernel, libdevel, libs, lisp, localization, mail, math,
     * metapackages, misc, net, news, ocaml, oldlibs, otherosfs, perl, php, python, ruby, science, shells, sound, tasks,
     * tex, text, utils, vcs, video, web, x11, xfce, zope. The section can be prefixed with contrib or non-free, if
     * not part of main.
     */
    @Input @Optional
    String packageGroup

    @Input @Optional
    String buildHost

    @Input @Optional
    String summary

    @Input @Optional
    String packageDescription

    @Input @Optional
    String license

    @Input @Optional
    String packager

    @Input @Optional
    String distribution

    @Input @Optional
    String vendor

    @Input @Optional
    String url

    @Input @Optional
    String sourcePackage

    @Input @Optional
    String provides

    // For Backward compatibility for those that passed in a Architecture object
    String archStr // This is what can be convention mapped and then referenced

    @Input @Optional
    void setArch(Object arch) {
        archStr = (arch instanceof Architecture)?arch.name():arch.toString()
    }

    // Type of getter has to match setter
//    Object getArch() {
//        return getArchStr();
//    }

    // RPM Only

    @Input @Optional
    Directive fileType

    @Input @Optional
    Boolean createDirectoryEntry

    @Input @Optional
    Boolean addParentDirs

    @Input @Optional
    Os os

    @Input @Optional
    RpmType type

    List<String> prefixes = new ArrayList<String>()

    def prefix(String prefixStr) {
        prefixes << prefixStr
        return this
    }

    // DEB Only

    @Input @Optional
    Integer uid

    @Input @Optional
    Integer gid

    @Input @Optional
    MultiArch multiArch

    @Input @Optional
    String maintainer

    @Input @Optional
    String uploaders
    
    @Input @Optional
    String priority

    @Input @Optional
    final List<Object> supplementaryControlFiles = []

    def supplementaryControl(String file) {
        supplementaryControlFiles << file
        return this
    }

    def supplementaryControl(File file) {
        supplementaryControlFiles << file
        return this
    }

    // Scripts

    final List<Object> configurationFiles = []

    final List<Object> preInstallCommands = []

    final List<Object> postInstallCommands = []

    final List<Object> preUninstallCommands = []

    final List<Object> postUninstallCommands = []

    final List<Object> commonCommands = []

    /**
     * For backwards compatibility
     * @param script
     */
    def setInstallUtils(File script) {
        installUtils(script)
    }

    def installUtils(String script) {
        commonCommands << script
        return this
    }

    def installUtils(File script) {
        commonCommands << script
        return this
    }

    /**
     * For backwards compatibility
     * @param script
     */
    def setConfigurationFile(String script) {
        configurationFile(script)
    }

    def configurationFile(String path) {
        configurationFiles << path
        return this
    }

    /**
     * For backwards compatibility
     * @param script
     */
    def setPreInstall(File script) {
        preInstall(script)
    }

    def preInstall(String script) {
        preInstallCommands << script
        return this
    }

    def preInstall(File script) {
        preInstallCommands << script
        return this
    }

    /**
     * For backwards compatibility
     * @param script
     */
    def setPostInstall(File script) {
        postInstall(script)
    }

    def postInstall(String script) {
        postInstallCommands << script
        return this
    }

    def postInstall(File script) {
        postInstallCommands << script
        return this
    }


    /**
     * For backwards compatibility
     * @param script
     */
    def setPreUninstall(File script) {
        preUninstall(script)
    }

    def preUninstall(String script) {
        preUninstallCommands << script
        return this
    }

    def preUninstall(File script) {
        preUninstallCommands << script
        return this
    }

    /**
     * For backwards compatibility
     * @param script
     */
    def setPostUninstall(File script) {
        postUninstall(script)
    }

    def postUninstall(String script) {
        postUninstallCommands << script
        return this
    }

    def postUninstall(File script) {
        postUninstallCommands << script
        return this
    }

    // @groovy.transform.PackageScope doesn't seem to set the proper scope when going through a @Delegate

    List<Link> links = new ArrayList<Link>()
    Link link(String path, String target) {
        link(path, target, -1)
    }

    Link link(String path, String target, int permissions) {
        Link link = new Link()
        link.path = path
        link.target = target
        link.permissions = permissions
        links.add(link)
        link
    }

    List<Dependency> dependencies = new ArrayList<Dependency>()
    List<Dependency> obsoletes = new ArrayList<Dependency>()
    List<Dependency> conflicts = new ArrayList<Dependency>()
    // Deb-specific special dependencies
    List<Dependency> recommends = new ArrayList<Dependency>()
    List<Dependency> suggests = new ArrayList<Dependency>()
    List<Dependency> enhances = new ArrayList<Dependency>()
    List<Dependency> preDepends = new ArrayList<Dependency>()
    List<Dependency> breaks = new ArrayList<Dependency>()
    List<Dependency> replaces = new ArrayList<Dependency>()

    Dependency requires(String packageName, String version, int flag) {
        def dep = new Dependency(packageName, version, flag)
        dependencies.add(dep)
        dep
    }

    Dependency requires(String packageName) {
        requires(packageName, '', 0)
    }

    Dependency obsoletes(String packageName, String version, int flag) {
        def dep = new Dependency(packageName, version, flag)
        obsoletes.add(dep)
        dep
    }

    Dependency obsoletes(String packageName) {
        obsoletes(packageName, '', 0)
    }

    Dependency conflicts(String packageName, String version, int flag) {
        def dep = new Dependency(packageName, version, flag)
        conflicts.add(dep)
        dep
    }

    Dependency conflicts(String packageName) {
        conflicts(packageName, '', 0)
    }

    Dependency recommends(String packageName, String version, int flag) {
        def dep = new Dependency(packageName, version, flag)
        recommends.add(dep)
        dep
    }

    Dependency recommends(String packageName) {
        recommends(packageName, '', 0)
    }

    Dependency suggests(String packageName, String version, int flag) {
        def dep = new Dependency(packageName, version, flag)
        suggests.add(dep)
        dep
    }

    Dependency suggests(String packageName) {
        suggests(packageName, '', 0)
    }

    Dependency enhances(String packageName, String version, int flag) {
        def dep = new Dependency(packageName, version, flag)
        enhances.add(dep)
        dep
    }

    Dependency enhances(String packageName) {
        enhances(packageName, '', 0)
    }

    Dependency preDepends(String packageName, String version, int flag) {
        def dep = new Dependency(packageName, version, flag)
        preDepends.add(dep)
        dep
    }

    Dependency preDepends(String packageName) {
        preDepends(packageName, '', 0)
    }

    Dependency breaks(String packageName, String version, int flag) {
        def dep = new Dependency(packageName, version, flag)
        breaks.add(dep)
        dep
    }

    Dependency breaks(String packageName) {
        breaks(packageName, '', 0)
    }

    Dependency replaces(String packageName, String version, int flag) {
        def dep = new Dependency(packageName, version, flag)
        replaces.add(dep)
        dep
    }

    Dependency replaces(String packageName) {
        replaces(packageName, '', 0)
    }

    List<Directory> directories = new ArrayList<Directory>()

    Directory directory(String path) {
        Directory directory = directory(path, -1)
        directories << directory
        directory
    }

    Directory directory(String path, int permissions) {
        Directory directory = new Directory(path: path, permissions: permissions)
        directories << directory
        directory
    }
}
