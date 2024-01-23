import io.cmt.gradle.CmtBuildUtils

extra["sharedProjectVersion"] = CmtBuildUtils.semver(major = 0, minor = 1, patch = 0, pre = CmtBuildUtils.PRE_SNAPSHOT)
