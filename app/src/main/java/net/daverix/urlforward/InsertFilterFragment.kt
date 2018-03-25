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

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.*
import dagger.android.support.DaggerFragment
import net.daverix.urlforward.databinding.InsertFilterFragmentBinding
import javax.inject.Inject

class InsertFilterFragment : DaggerFragment() {
    @set:[Inject]
    lateinit var viewModel: InsertFilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        if (savedInstanceState != null) {
            viewModel.restoreInstanceState(savedInstanceState)
        }
    }

    override fun onDestroy() {
        viewModel.onDestroy()

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel.saveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<InsertFilterFragmentBinding>(inflater, R.layout.insert_filter_fragment, container, false)?.apply {
            this.viewModel = viewModel
        }?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(savedInstanceState == null) {
            viewModel.loadFilter()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.fragment_insert_filter, menu)
    }

    companion object {
        fun newInstance() = InsertFilterFragment().apply {
            arguments = Bundle()
        }
    }
}
