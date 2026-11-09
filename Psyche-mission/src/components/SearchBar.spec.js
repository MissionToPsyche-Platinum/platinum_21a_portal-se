import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import SearchBar from './SearchBar.vue'

describe('SearchBar', () => {
  it('emits the typed search text', async () => {
    const wrapper = mount(SearchBar)
    const input = wrapper.get('input')

    await input.setValue('asteroid')

    expect(wrapper.emitted('search')).toBeTruthy()
    expect(wrapper.emitted('search').at(-1)[0]).toBe('asteroid')
  })

  it('clears the search box and emits an empty string', async () => {
    const wrapper = mount(SearchBar)
    await wrapper.get('input').setValue('asteroid')

    wrapper.vm.clear()
    await wrapper.vm.$nextTick()

    expect(wrapper.get('input').element.value).toBe('')
    expect(wrapper.emitted('search').at(-1)[0]).toBe('')
  })
})
