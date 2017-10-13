package dinson.customview.kotlin

import android.os.Parcel
import android.os.Parcelable

/**
 *   @author Dinson - 2017/10/13
 */

data class Github(
    val archive_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/{archive_format}{/ref}
    val assignees_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/assignees{/user}
    val blobs_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/git/blobs{/sha}
    val branches_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/branches{/branch}
    val clone_url: String, //https://github.com/DinsonCat/BlingBling.git
    val collaborators_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/collaborators{/collaborator}
    val comments_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/comments{/number}
    val commits_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/commits{/sha}
    val compare_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/compare/{base}...{head}
    val contents_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/contents/{+path}
    val contributors_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/contributors
    val created_at: String, //2017-07-24T14:03:47Z
    val default_branch: String, //master
    val deployments_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/deployments
    val description: String, //:octocat: CustomView Set
    val downloads_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/downloads
    val events_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/events
    val fork: Boolean, //false
    val forks: Int, //0
    val forks_count: Int, //0
    val forks_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/forks
    val full_name: String, //DinsonCat/BlingBling
    val git_commits_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/git/commits{/sha}
    val git_refs_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/git/refs{/sha}
    val git_tags_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/git/tags{/sha}
    val git_url: String, //git://github.com/DinsonCat/BlingBling.git
    val has_downloads: Boolean, //true
    val has_issues: Boolean, //true
    val has_pages: Boolean, //false
    val has_projects: Boolean, //true
    val has_wiki: Boolean, //true
    val homepage: String,
    val hooks_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/hooks
    val html_url: String, //https://github.com/DinsonCat/BlingBling
    val id: Int, //98197381
    val issue_comment_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/issues/comments{/number}
    val issue_events_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/issues/events{/number}
    val issues_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/issues{/number}
    val keys_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/keys{/key_id}
    val labels_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/labels{/name}
    val language: String, //Java
    val languages_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/languages
    val merges_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/merges
    val milestones_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/milestones{/number}
    val name: String, //BlingBling
    val notifications_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/notifications{?since,all,participating}
    val open_issues: Int, //0
    val open_issues_count: Int, //0
    val owner: Owner,
    val private: Boolean, //false
    val pulls_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/pulls{/number}
    val pushed_at: String, //2017-10-12T08:46:05Z
    val releases_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/releases{/id}
    val size: Int, //1516
    val ssh_url: String, //git@github.com:DinsonCat/BlingBling.git
    val stargazers_count: Int, //0
    val stargazers_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/stargazers
    val statuses_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/statuses/{sha}
    val subscribers_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/subscribers
    val subscription_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/subscription
    val svn_url: String, //https://github.com/DinsonCat/BlingBling
    val tags_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/tags
    val teams_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/teams
    val trees_url: String, //https://api.github.com/repos/DinsonCat/BlingBling/git/trees{/sha}
    val updated_at: String, //2017-09-19T00:45:46Z
    val url: String, //https://api.github.com/repos/DinsonCat/BlingBling
    val watchers: Int, //0
    val watchers_count: Int //0
)

data class Owner(
    val avatar_url: String, //https://avatars0.githubusercontent.com/u/17812321?v=4
    val events_url: String, //https://api.github.com/users/DinsonCat/events{/privacy}
    val followers_url: String, //https://api.github.com/users/DinsonCat/followers
    val following_url: String, //https://api.github.com/users/DinsonCat/following{/other_user}
    val gists_url: String, //https://api.github.com/users/DinsonCat/gists{/gist_id}
    val gravatar_id: String,
    val html_url: String, //https://github.com/DinsonCat
    val id: Int, //17812321
    val login: String, //DinsonCat
    val organizations_url: String, //https://api.github.com/users/DinsonCat/orgs
    val received_events_url: String, //https://api.github.com/users/DinsonCat/received_events
    val repos_url: String, //https://api.github.com/users/DinsonCat/repos
    val site_admin: Boolean, //false
    val starred_url: String, //https://api.github.com/users/DinsonCat/starred{/owner}{/repo}
    val subscriptions_url: String, //https://api.github.com/users/DinsonCat/subscriptions
    val type: String, //User
    val url: String //https://api.github.com/users/DinsonCat
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        1 == source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(avatar_url)
        writeString(events_url)
        writeString(followers_url)
        writeString(following_url)
        writeString(gists_url)
        writeString(gravatar_id)
        writeString(html_url)
        writeInt(id)
        writeString(login)
        writeString(organizations_url)
        writeString(received_events_url)
        writeString(repos_url)
        writeInt((if (site_admin) 1 else 0))
        writeString(starred_url)
        writeString(subscriptions_url)
        writeString(type)
        writeString(url)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Owner> = object : Parcelable.Creator<Owner> {
            override fun createFromParcel(source: Parcel): Owner = Owner(source)
            override fun newArray(size: Int): Array<Owner?> = arrayOfNulls(size)
        }
    }
}
