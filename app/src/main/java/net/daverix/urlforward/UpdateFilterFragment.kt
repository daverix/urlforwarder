/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2017 David Laurell

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

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import dagger.android.support.DaggerFragment
import net.daverix.urlforward.databinding.UpdateFilterFragmentBinding
import javax.inject.Inject

class UpdateFilterFragment : DaggerFragment() {
    @set:Inject
    lateinit var viewModel: UpdateFilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        if (savedInstanceState != null) {
            viewModel.restoreInstanceState(savedInstanceState)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<UpdateFilterFragmentBinding>(inflater,
                R.layout.update_filter_fragment,
                container,
                false).apply {
            this.viewModel = viewModel
            setLifecycleOwner(this@UpdateFilterFragment)
        }?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(savedInstanceState == null) {
            viewModel.loadFilter()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel.saveInstanceState(outState)
    }

    override fun onDestroy() {
        viewModel.onDestroy()

        super.onDestroy()
    }

    companion object {
        const val ARG_FILTER_ID = "filterId"

        fun newInstance(filterId: Long) = UpdateFilterFragment().apply {
            arguments = bundleOf(ARG_FILTER_ID to filterId)
        }
    }
}
