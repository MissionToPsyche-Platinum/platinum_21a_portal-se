import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import SearchBar from "../src/components/SearchBar.vue";

describe("SearchBar", () => {
    it("emits the search text when the user types", async () => {
        const wrapper = mount(SearchBar);
        const input = wrapper.find("input");

        await input.setValue("Psyche Scraper");

        expect(wrapper.emitted("search")).toBeTruthy();
        expect(wrapper.emitted("search")[0]).toEqual(["Psyche Scraper"]);
    })

    it("emits the search text when the Search button is clicked", async () => {
        const wrapper = mount(SearchBar);
        const input = wrapper.find("input");

        await input.setValue("Astro Academy");
        await wrapper.find("button").trigger("click");

        expect(wrapper.emitted("search")).toBeTruthy();
        expect(wrapper.emitted("search").at(-1)).toEqual(["Astro Academy"]);
    })
})

