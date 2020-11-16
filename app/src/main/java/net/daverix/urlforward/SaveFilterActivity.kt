/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2016 David Laurell

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daverix.urlforward

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import net.daverix.urlforward.FilterService
import net.daverix.urlforward.SaveFilterFragment.Companion.newCreateInstance
import net.daverix.urlforward.SaveFilterFragment.Companion.newUpdateInstance
import net.daverix.urlforward.databinding.SaveFilterActivityBinding

class SaveFilterActivity : AppCompatActivity() {
    private var fragment: SaveFilterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: SaveFilterActivityBinding = DataBindingUtil.setContentView(this, R.layout.save_filter_activity)

        binding.toolbar.setNavigationOnClickListener {
            val intent = Intent(this@SaveFilterActivity, FiltersActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        fragment = supportFragmentManager.findFragmentById(R.id.saveFilterFragment) as SaveFilterFragment?

        when (intent?.action) {
            Intent.ACTION_INSERT -> {
                binding.toolbar.setTitle(R.string.create_filter)
                binding.toolbar.inflateMenu(R.menu.fragment_save_filter)
                if (fragment == null) {
                    fragment = newCreateInstance()
                    supportFragmentManager.beginTransaction().add(R.id.saveFilterFragment, fragment!!).commit()
                }
            }
            Intent.ACTION_EDIT -> {
                binding.toolbar.setTitle(R.string.edit_filter)
                binding.toolbar.inflateMenu(R.menu.fragment_edit_filter)
                if (fragment == null) {
                    fragment = newUpdateInstance(intent.data)
                    supportFragmentManager.beginTransaction().add(R.id.saveFilterFragment, fragment!!).commit()
                }
            }
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuSave -> {
                    when (intent?.action) {
                        Intent.ACTION_INSERT -> createFilter(fragment!!.filter)
                        Intent.ACTION_EDIT -> updateFilter(fragment!!.filter, intent.data)
                    }
                    true
                }
                R.id.menuDelete -> {
                    val data = intent?.data
                    if (data != null) {
                        deleteFilter(data)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun createFilter(linkFilter: LinkFilter?) {
        startService(Intent(this, FilterService::class.java).apply {
            action = Intent.ACTION_INSERT
            putExtra(FilterService.EXTRA_LINK_FILTER, linkFilter)
        })
        finish()
    }

    private fun updateFilter(linkFilter: LinkFilter?, uri: Uri?) {
        startService(Intent(this, FilterService::class.java).apply {
            action = Intent.ACTION_EDIT
            data = uri
            putExtra(FilterService.EXTRA_LINK_FILTER, linkFilter)
        })
        finish()
    }

    private fun deleteFilter(uri: Uri) {
        startService(Intent(this, FilterService::class.java).apply {
            action = Intent.ACTION_DELETE
            data = uri
        })
        finish()
    }
}