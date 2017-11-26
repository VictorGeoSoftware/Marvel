package com.test.marvel.vallettest.models

import android.util.Log

/**
 * Created by victor on 21/11/17.
 */
data class Comic(val id:Long,
                 val digitalId:Int,
                 val title:String,
                 val issueNumber:Double,
                 val variantDescription:String,
                 val description:String,
                 val modified:String,
                 val isbn:String,
                 val upc:String,
                 val diamondCode:String,
                 val ean:String,
                 val issn:String,
                 val format:String,
                 val pageCount:Int,
                 val textObjects:ArrayList<TextObject>,
                 val resourceURI:String,
                 val urls:ArrayList<Url>,
                 val series:SeriesSummary,
                 val variants:ArrayList<ComicSummary>,
                 val collections:ArrayList<ComicSummary>,
                 val collectedIssues:ArrayList<ComicSummary>,
                 val dates:ArrayList<ComicDate>,
                 val prices:ArrayList<ComicDate>,
                 val thumbnail:Image,
                 val images:ArrayList<Image>,
                 val creators:CreatorList,
                 val characters:CharacterList,
                 val stories:StoryList,
                 val events:EventList) : Comparable<Comic> {

    private fun getIndex() : Double {
        return if (this.issueNumber == 0.0) {
            Double.MAX_VALUE
        } else{
            this.issueNumber
        }
    }

    override fun compareTo(other: Comic): Int =
        compareValuesBy(
                this,
                other,
                compareBy(nullsLast(), Comic::getIndex).thenBy(Comic::title),
                { it }
        )

}